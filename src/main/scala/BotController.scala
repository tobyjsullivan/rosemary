
object BotController {

  def respond(input: String): String = Command.parse(input) match {
    case welcome @ Command("Welcome", _) => respondToWelcome(welcome)
    case react @ Command("React", _) => respondToReact(react)
    case goodbye @ Command("Goodbye", _) => respondToGoodbye(goodbye)
    case _ => ""
  }

  private def respondToWelcome(welcome: Command): String = {
    val apoc = welcome.params("apocalypse")
    val maxSlaves = welcome.params.getOrElse("maxslaves", Config.DefaultMaxSlaves.toString)

    Command("Set", Map("apocalypse" -> apoc, "maxslaves" -> maxSlaves)).toString
  }

  private def respondToReact(react: Command): String = {
    try {
      val state = new State(react)

      val mode: ThoughtMode = state.mode

      val status = if(Config.Debug) Some(Command("Status", Map("text" -> state.name.getOrElse("unknown")))) else None

      Command.compose(mode.react(state) ++ status.toList)
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
