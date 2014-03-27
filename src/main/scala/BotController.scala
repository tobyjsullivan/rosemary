
object BotController {
  val Debug = true
  val Slaves = 10

  def respond(input: String): String = Command.parse(input) match {
    case welcome @ Command("Welcome", _) => respondToWelcome(welcome)
    case react @ Command("React", _) => respondToReact(react)
    case goodbye @ Command("Goodbye", _) => respondToGoodbye(goodbye)
    case _ => ""
  }

  private def respondToWelcome(welcome: Command): String = {
    val apoc = welcome.params("apocalypse")

    Command("Set", Map("apocalypse" -> apoc)).toString
  }

  private def respondToReact(react: Command): String = {
    try {
      val state = new State(react)

      // Master bot should update relPos
      val relPos: Point = if(state.generation.getOrElse(-1) == 0) (state.recall("RelPos"), state.collision) match {
        case (Some(sPos), Some(collision)) => {
          val pos = Point.parse(sPos)
          pos - collision
        }
        case (Some(sPos), None) => Point.parse(sPos)
        case ( None, _) => Point(0,0)
      } else (state.recall("RelPos").map(s => Point.parse(s)).getOrElse(Point(0,0)) + state.master.getOrElse(Point(0,0)))

      val cerebrum = new Cerebrum(state)
      val bestDir = cerebrum.bestDirection.truncate

      val newRelPos = relPos + bestDir
      state.remember("RelPos", newRelPos.toString)

      val status = if(Debug) Some(Command("Status", Map("text" -> relPos.toString))) else None

      val slaves = state.slaves.getOrElse(0)
      val energy = state.energy.getOrElse(0)

      val spawn = if(energy > 1000 && slaves < Slaves) Some(Command("Spawn", Map())) else None

      val cmd = Command("Move", Map("direction" -> bestDir.toString))

      // Get new memories to Set(...)
      val memories = state.memoryConsolidation()

      Command.compose(Seq(cmd, memories) ++ status.toList ++ spawn.toList)
    } catch {
      case e: Exception => {
        println(e.getMessage())
        println(e.getStackTraceString)
        "Say("+e.getMessage()+")"
      }
    }
  }

  private def respondToGoodbye(goodbye: Command): String = ""
}
