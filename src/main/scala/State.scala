class State(react: Command) {
  require(react.opcode == "React", "States can only be generated for React commands")

  lazy val generation: Option[Int] = react.params.get("generation").map(s => s.toInt)
  lazy val energy: Option[Int] = react.params.get("energy").map(s => s.toInt)
  lazy val collision: Option[Point] = react.params.get("collision").map(s => Point.parse(s))

  lazy val view: Option[Vision] = react.params.get("view").map(s => new Vision(s))

  private var _memory: Map[String, String] = Map()
  private var _dirtyThoughts: Set[String] = Set()
  def remember(key: String, value: String) {
    _memory += key -> value
    _dirtyThoughts += key
  }

  def recall(key: String): Option[String] = {
    val result = _memory.get(key)
    result
  }

  def memoryConsolidation(): Command = {
    val changed = _memory.filter(kv => _dirtyThoughts.contains(kv._1))

    Command("Set", changed)
  }
}
