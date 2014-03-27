trait ThoughtMode {
  def react(state: State): Seq[Command]
}
