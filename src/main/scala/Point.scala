case class Point(x: Int, y: Int) {
  def toDirectionString: String = {
    val x = math.max(-1, math.min(1, this.x))
    val y = math.max(-1, math.min(1, this.y))

    ""+x+":"+y
  }
}
