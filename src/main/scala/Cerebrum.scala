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

  private lazy val nearbySnorgs = state.view.map(v => v.findAll('b').filter(p => Hippocampus.distance(Point(0,0), p) <= 3.0)).getOrElse(Set())
  private lazy val nearbyEnemyMinibots = state.view.map(v => v.findAll('s')).getOrElse(Set())
  private lazy val nearbyZugar = state.view.map(v => v.findAll('P')).getOrElse(Set())
  private lazy val nearbyFluppets = state.view.map(v => v.findAll('B')).getOrElse(Set())

  private lazy val openHeadings: Set[Point] = {
    val range = state.view.map(v => v.range).getOrElse(0)
    val allHeadings = for {
      x <- (0 - range) to range
      y <- (0 - range) to range
      heading = Point(x, y)
    } yield heading

    allHeadings.filter(heading => {
      val c = state.view.map(v => v.lookAt(heading)).getOrElse('_')
      c != '?' && c != 'W'
    }).toSet
  }

  lazy val escapeRoute: Option[Point] = {
    if(nearbySnorgs.size > 0 || nearbyEnemyMinibots.size > 0)
      Some(Hippocampus.findClosest(Point(0, 0), nearbySnorgs ++ nearbyEnemyMinibots).invert)
    else None
  }

  lazy val foodDirection: Option[Point] = {
    if(nearbyZugar.size > 0)
      Some(Hippocampus.findClosest(Point(0, 0), nearbyZugar))
    else if(nearbyFluppets.size > 0)
      Some(Hippocampus.findClosest(Point(0, 0), nearbyFluppets))
    else None
  }

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
