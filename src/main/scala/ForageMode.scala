object ForageMode extends ThoughtMode {
  val id = "forage"

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

    val nearbyEnemyBot = state.view.flatMap(v => nearestEnemyMiniBot(v))

    // Spawn an intercept bot if there's a nearby enemy minibot.
    // Otherwise, spawn an extra forage bot (if enough room and energy)
    val spawn = if (nearbyEnemyBot.isDefined) Some(Command("Spawn", Map(
      "name" -> InterceptMode.id,
      "energy" -> Config.InterceptBotEnergy.toString,
      "direction" -> nearbyEnemyBot.get.truncate.toString)))
    else if (energy > 1000 && slaves < maxSlaves) Some(Command("Spawn", Map(
      "name" -> ForageMode.id,
      "direction" -> bestDir.invert.toString)))
    else None

    val cmd = Command("Move", Map("direction" -> bestDir.toString))

    // Get new memories to Set(...)
    val memories = state.memoryConsolidation()

    Seq(cmd, memories) ++ status.toList ++ spawn.toList
  }

  def nearestEnemyMiniBot(view: Vision): Option[Point] = {
    val allEnemyMiniBots: Set[Point] = view.findAll('s')
    if(allEnemyMiniBots.size > 0) Some(Hippocampus.findClosest(Point(0,0), allEnemyMiniBots)) else None
  }
}
