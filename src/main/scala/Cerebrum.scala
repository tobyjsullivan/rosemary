import scala.util.Random

class Cerebrum(state: State) {
  private val rand = new Random()

  private lazy val nearbySnorgs = state.view.map(v => v.findAll('b')).getOrElse(Set())
  private lazy val nearbyEnemyMinibots = state.view.map(v => v.findAll('s')).getOrElse(Set())
  private lazy val nearbyZugar = state.view.map(v => v.findAll('P')).getOrElse(Set())

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

  lazy val isUnderThreat: Boolean = nearbySnorgs.size > 0 || nearbyEnemyMinibots.size > 0

  lazy val escapeRoute: Option[Point] = if(isUnderThreat) Some(Hippocampus.findClosest(Point(0, 0), nearbySnorgs ++ nearbyEnemyMinibots).invert) else None

  lazy val isNearFood: Boolean = nearbyZugar.size > 0

  lazy val foodDirection: Option[Point] = if(isNearFood) Some(Hippocampus.findClosest(Point(0, 0), nearbyZugar)) else None

  lazy val bestDirection: Point = (escapeRoute, foodDirection) match {
    case (Some(d), _) => Hippocampus.findClosest(d, openHeadings)
    case (None, Some(d)) => d
    case (None, None) => {
      // If no threats and no food, choose an open heading that is free to explore
      openHeadings.toList(rand.nextInt(openHeadings.size))
    }
  }


}
