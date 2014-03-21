package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class BotControllerSpec extends FlatSpec with Matchers {
  "respond" should "respond to a welcome message by setting an apocalypse date" in {
    val apocalypse = 459
    val input = "Welcome(name=some_name,apocalypse="+apocalypse+",round=9)"

    val response = BotController.respond(input)

    response should include ("Set(apocalypse="+apocalypse+")")
  }
}
