package scalatron.botwar.botPlugin

class Vision(input: String) {
  require(input.size > 0, "Input must not be an empty string")
  require(input.filterNot(c => "MmSsBbPpW_?".contains(c)).size == 0, "The input string includes unacceptable characters")

  val size: Int = math.sqrt(input.size).toInt
  require(size * size == input.size, "Length of input string must be a perfect square")

  lazy val center = Point(size/2, size/2)
  lazy val range = size/2
  
  def lookAt(position: Point): Char = {
    require(position.x >= 0-range)
    require(position.x <= range)
    require(position.y >= 0-range)
    require(position.y <= range)

    val idx = relPositionAsIndex(position)
    input(idx)
  }
  
  private def relPositionAsIndex(position: Point) = {
    val absX = position.x + range
    val absY = position.y + range

    size * absY + absX
  }

  override def toString: String = input

  override def equals(o: Any) = o match {
    case o: Vision => this.toString == o.toString
    case _ => false
  }

  override def hashCode = this.input.hashCode()
}
