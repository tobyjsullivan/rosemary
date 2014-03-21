package scalatron.botwar.botPlugin

import util.Random

private[botPlugin] object BotController {
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
    val state = new State(react)

    // Look for food
    val zugar = state.view.map(v => v.findAll('P')).map(l => Hippocampus.findClosest(Point(0,0), l))

    // If zugar is near, move towards. Otherwise random
    val dir = zugar match {
      case Some(p) => p
      case None => {
        val rand = new Random()
        Point(rand.nextInt(3)-1, rand.nextInt(3)-1)
      }
    }

    val cmd = Command("Move", Map("direction" -> dir.toDirectionString))

    cmd.toString
  }

  private def respondToGoodbye(goodbye: Command): String = ""
}
