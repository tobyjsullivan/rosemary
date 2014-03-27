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

    lazy val nearbyEnemyMiniBot = state.view.flatMap(v => nearestElement(v, 's'))
    lazy val nearbyEnemyMasterBot = state.view.flatMap(v => nearestElement(v, 'm'))

    // Spawn an intercept bot if there's a nearby enemy minibot.
    // Otherwise, spawn an extra forage bot (if enough room and energy)
    val spawn = if (energy > 5000 && nearbyEnemyMiniBot.isDefined) Some(Command("Spawn", Map(
      "name" -> InterceptMode.id,
      "energy" -> Config.InterceptBotEnergy.toString,
      "direction" -> nearbyEnemyMiniBot.get.truncate.toString)))
    else if (energy > 25000 && nearbyEnemyMasterBot.isDefined) Some(Command("Spawn", Map(
      "name" -> MissileMode.id,
      "energy" -> Config.MissileBotEnergy.toString,
      "direction" -> nearbyEnemyMasterBot.get.truncate.toString)))
    else if (energy > 1000 && slaves < maxSlaves) Some(Command("Spawn", Map(
      "name" -> ForageMode.id,
      "energy" -> Config.ForageBotEnergy.toString,
      "direction" -> bestDir.invert.toString)))
    else None

    val cmd = Command("Move", Map("direction" -> bestDir.toString))

    if(cerebrum.headToMaster && WallMode.inCorner(state)) {
      val masterHeading = state.master.getOrElse(Point(0,0))
      WallMode.initVals(newRelPos, masterHeading, state)
    }

    // Get new memories to Set(...)
    val memories = state.memoryConsolidation()

    Seq(cmd, memories) ++ status.toList ++ spawn.toList
  }

  def nearestElement(view: Vision, el: Char): Option[Point] = {
    val allEnemyMiniBots: Set[Point] = view.findAll(el)
    if(allEnemyMiniBots.size > 0) Some(Hippocampus.findClosest(Point(0,0), allEnemyMiniBots)) else None
  }
}
