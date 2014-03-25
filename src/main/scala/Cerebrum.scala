import scala.util.Random

class Cerebrum(state: State) {
  private val rand = new Random()

  private val EntityWeightHalflife: Map[Char,Pair[Int,Int]] = Map(
    'P' -> (100, 4),
    'p' ->(-100, 0),
    'B' ->(200, 2),
    'b' ->(-150, 1),
    'm' ->(-2000, 0),
    's' ->(-200, 4),
    'W' ->(-2000, 0)
  )

  lazy val bestDirection: Point = {
    val moves = getPossibleMoves()

    val rewardMap = moves.map(p => (p -> calcRiskRewardRatio(p)))

    println(rewardMap)

    val res = rewardMap.toSeq.sortBy(kv => kv._2).last
    println("Expected reward: " + res._2)
    res._1
  }

  private def getPossibleMoves(pos: Point = Point(0, 0)): Set[Point] = {
    val res = for {
      x: Int <- -1 to 1
      y: Int <- -1 to 1
      point = Point(x, y) if !(x == 0 && y == 0)
    } yield pos + point

    res.toSet
  }

  def calcRiskRewardRatio(pos: Point, subs: Int = 1): Double = {
    val ratios = for {
      (entity, (weight, halflife)) <- EntityWeightHalflife
      inst <- state.view.map(v => v.findAll(entity)).getOrElse(Set())
      distance = math.max(Hippocampus.distance(pos, inst), 0.1)
      ratio = if (halflife == 0 && inst == pos) weight.toDouble else (weight.toDouble * (halflife.toDouble / distance))
    } yield ratio

    ratios.foldRight(0.0)((z, r) => z + r)
  }
}
