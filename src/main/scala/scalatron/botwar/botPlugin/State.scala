package scalatron.botwar.botPlugin

class State(react: Command) {
  require(react.opcode == "React", "States can only be generated for React commands")

  lazy val generation: Option[Int] = react.params.get("generation").map(s => s.toInt)
  lazy val energy: Option[Int] = react.params.get("energy").map(s => s.toInt)

  lazy val view: Option[Vision] = react.params.get("view").map(s => new Vision(s))
}
