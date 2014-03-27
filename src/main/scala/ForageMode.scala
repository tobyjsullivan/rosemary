object ForageMode extends ThoughtMode {
  def react(state: State): Seq[Command] = {
    val maxSlaves = state.recall("maxslaves").map(s => s.toInt).getOrElse(Config.DefaultMaxSlaves)

    // Master bot should update relPos
    val relPos: Point = (state.recall("RelPos"), state.collision) match {
      case (Some(sPos), Some(collision)) => {
        val pos = Point.parse(sPos)
        pos - collision
      }
      case (Some(sPos), None) => Point.parse(sPos)
      case ( None, _) => Point(0,0)
    }

    val cerebrum = new Cerebrum(state)
    val bestDir = cerebrum.bestDirection.truncate

    val newRelPos = relPos + bestDir
    state.remember("RelPos", newRelPos.toString)

    val status = if(Config.Debug) Some(Command("Status", Map("text" -> state.energy.getOrElse(0).toString))) else None

    val slaves = state.slaves.getOrElse(0)
    val energy = state.energy.getOrElse(0)

    val spawn = if(energy > 1000 && slaves < maxSlaves) Some(Command("Spawn", Map())) else None

    val cmd = Command("Move", Map("direction" -> bestDir.toString))

    // Get new memories to Set(...)
    val memories = state.memoryConsolidation()

    Seq(cmd, memories) ++ status.toList ++ spawn.toList
  }
}
