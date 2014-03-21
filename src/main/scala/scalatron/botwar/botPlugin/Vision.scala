package scalatron.botwar.botPlugin

class Vision(input: String) {
  val size = math.sqrt(input.size)

  require(size * size == input.size, "Length of input string must be a perfect square")
  require(input.filterNot(c => "MmSsBbPpW_?".contains(c)).size == 0, "The input string includes unacceptable characters")
}
