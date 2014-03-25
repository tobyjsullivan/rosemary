import util.Random

object BotController {
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
      val cerebrum = new Cerebrum(state)
      val dir = cerebrum.bestDirection

      // Gen a list of possible moves
      val moves = for {
        x: Int <- (-1 to 1).toSet
        y: Int <- -1 to 1
        point = Point(x, y)
      } yield point

      // Find the best available move that is not blocked
      val view = state.view.get
      val bestDir = Hippocampus.findClosest(dir, moves.filter(p => view.lookAt(p) != 'W' && view.lookAt(p) != 'p'))

      val cmd = Command("Move", Map("direction" -> bestDir.toDirectionString))

      // Get new memories to Set(...)
      val memories = state.memoryConsolidation()

      Command.compose(Seq(cmd, memories))
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
