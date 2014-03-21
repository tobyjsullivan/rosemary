package scalatron.botwar.botPlugin

private[botPlugin] object ControlFunctionFactory {
  def create : (String => String) = BotController.respond
}
