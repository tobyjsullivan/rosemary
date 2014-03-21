package scalatron.botwar.botPlugin

/**
 * Created by tobys on 2014-03-20.
 */

object ControlFunctionFactory {
  def create : (String => String) = BotController.respond
}
