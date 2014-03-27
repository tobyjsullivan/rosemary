trait ThoughtMode {
  val id: String

  def react(state: State): Seq[Command]
}
