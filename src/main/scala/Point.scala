object Point {
  def parse(s: String): Point = {
    require(s.size >= 3, "Point string must be of format <X>:<Y>. Received: |"+s+"|")
    require(s.contains(":"), "Point string must be of format <X>:<Y>. Received: |"+s+"|")

    val pieces = s.split(":")
    val x = pieces(0).toInt
    val y = pieces(1).toInt

    Point(x, y)
  }
}

case class Point(x: Int, y: Int) {
  def toDirectionString: String = {
    val trunc = this.truncate

    ""+trunc.x+":"+trunc.y
  }

  def truncate: Point = {
    val x = math.max(-1, math.min(1, this.x))
    val y = math.max(-1, math.min(1, this.y))

    Point(x, y)
  }

  def invert: Point = {
    val x = 0 - this.x
    val y = 0 - this.y

    Point(x, y)
  }
}
