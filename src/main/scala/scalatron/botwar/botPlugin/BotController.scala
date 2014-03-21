package scalatron.botwar.botPlugin

object BotController {
  def respond(input: String): String = Command.parse(input) match {
    case welcome @ Command("Welcome", _) => respondToWelcome(welcome)
    case _ => ""
  }

  def respondToWelcome(welcome: Command): String = {
    val apoc = welcome.params("apocalypse")

    Command("Set", Map("apocalypse" -> apoc)).toString
  }
}
