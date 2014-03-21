package scalatron.botwar.botPlugin

object ControlFunctionFactory {
  def create : (String => String) = BotController.respond
}
