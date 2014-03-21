/**
 * The hippocampus is responsible for spatial navigation as well as short- and long-term memory
 */
object Hippocampus {
  def relPosition(a: Point, b: Point): Point = {
    Point(b.x - a.x, b.y - a.y)
  }

  def distance(a: Point, b: Point): Double = {
    val vector = relPosition(a, b)

    math.sqrt(vector.x * vector.x + vector.y * vector.y)
  }

  def findClosest(origin: Point, l: Set[Point]): Point = {
    require(l.size > 0, "List of possible points must not be empty")

    l.reduce{ (p1, p2) =>
      if (distance(origin, p1) <= distance(origin, p2)) p1
      else p2
    }
  }
}
