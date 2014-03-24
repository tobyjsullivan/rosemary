import scala.util.Random

class Cerebrum(state: State) {
  private lazy val nearbySnorgs = state.view.map(v => v.findAll('b')).getOrElse(Set())
  private lazy val nearbyEnemyMinibots = state.view.map(v => v.findAll('s')).getOrElse(Set())
  private lazy val nearbyZugar = state.view.map(v => v.findAll('P')).getOrElse(Set())

  lazy val isUnderThreat: Boolean = nearbySnorgs.size > 0 || nearbyEnemyMinibots.size > 0

  lazy val escapeRoute: Option[Point] = if(isUnderThreat) Some(Hippocampus.findClosest(Point(0, 0), nearbySnorgs ++ nearbyEnemyMinibots).invert) else None

  lazy val isNearFood: Boolean = nearbyZugar.size > 0

  lazy val foodDirection: Option[Point] = if(isNearFood) Some(Hippocampus.findClosest(Point(0, 0), nearbyZugar)) else None

  lazy val bestDirection: Point = (escapeRoute, foodDirection) match {
    case (Some(d), _) => d
    case (None, Some(d)) => d
    case (None, None) => {
      val rand = new Random()
      Point(rand.nextInt(3) - 1, rand.nextInt(3) - 1)
    }
  }


}
