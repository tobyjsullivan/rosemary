import scala.util.Random

class Cerebrum(state: State) {
  private val rand = new Random()

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

  lazy val bestDirection: Point = (escapeRoute, foodDirection) match {
    case (Some(d), _) => {
      println("I'm Scared!")
      Hippocampus.findClosest(d, openHeadings)
    }
    case (None, Some(d)) => {
      println("I see food!")
      d
    }
    case (None, None) => {
      println("I'm exploring")
      // If no threats and no food, choose an open heading that is free to explore
      openHeadings.toList(rand.nextInt(openHeadings.size))
    }
  }


}
