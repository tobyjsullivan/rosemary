class State(react: Command) {
  require(react.opcode == "React", "States can only be generated for React commands")

  lazy val generation: Option[Int] = react.params.get("generation").map(s => s.toInt)
  lazy val energy: Option[Int] = react.params.get("energy").map(s => s.toInt)
  lazy val collision: Option[Point] = react.params.get("collision").map(s => Point.parse(s))
  lazy val time: Option[Int] = react.params.get("time").map(s => s.toInt)
  lazy val slaves: Option[Int] = react.params.get("slaves").map(s => s.toInt)
  lazy val master: Option[Point] = react.params.get("master").map(s => Point.parse(s))
  lazy val name: Option[String] = react.params.get("name")

  lazy val mode: ThoughtMode = react.params.get("mode").map {
    case ForageMode.id => ForageMode
    case InterceptMode.id => InterceptMode
    case MissileMode.id => MissileMode
    case _ => Config.DefaultBotMode
  }.getOrElse(modeFromName)

  lazy val modeFromName: ThoughtMode = name.getOrElse("") match {
    case ForageMode.id => ForageMode
    case InterceptMode.id => InterceptMode
    case MissileMode.id => MissileMode
    case _ => Config.DefaultBotMode
  }

  lazy val view: Option[Vision] = react.params.get("view").map(s => new Vision(s))

  private var _memory: Map[String, String] = react.params
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
