package scalatron.botwar.botPlugin

class Vision(input: String) {
  require(input.filterNot(c => "MmSsBbPpW_?".contains(c)).size == 0, "The input string includes unacceptable characters")

  val size: Int = math.sqrt(input.size).toInt
  require(size * size == input.size, "Length of input string must be a perfect square")

  val center = Point(size/2, size/2)
}
