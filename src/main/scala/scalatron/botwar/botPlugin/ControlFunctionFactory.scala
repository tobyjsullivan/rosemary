package scalatron.botwar.botPlugin

class ControlFunctionFactory {
  def create : (String => String) = BotController.respond _
}
