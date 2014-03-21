package scalatron.botwar.botPlugin

class State(react: Command) {
  require(react.opcode == "React", "States can only be generated for React commands")

  lazy val energy: Option[Int] = react.params.get("energy").map(s => s.toInt)
}
