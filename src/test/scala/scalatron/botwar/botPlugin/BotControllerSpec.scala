package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class BotControllerSpec extends FlatSpec with Matchers {
  "respond" should "respond to a welcome command by setting an apocalypse date" in {
    val apocalypse = 459
    val input = "Welcome(name=some_name,apocalypse="+apocalypse+",round=9)"

    val response = BotController.respond(input)

    response should include ("Set(apocalypse="+apocalypse+")")
  }

  it should "respond to a goodbye command with no commands" in {
    BotController.respond("Goodbye(energy=12983)") should be ("")
  }

  /**
   * This requirement is only temporary. It will not hold as the bot gets smarter
   */
  it should "Move to nearby zugar" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val input = Command("React", Map("view" -> view.mkString)).toString()

    val response = BotController.respond(input)

    response shouldEqual ("Move(direction=-1:0)")
  }

  it should "Move randomly if there is no nearby zugar" in {
    val view = List(
      "__s_p",
      "W___b",
      "__MS_",
      "WWW_B",
      "????_"
    )

    val input = Command("React", Map("view" -> view.mkString)).toString()

    val response = BotController.respond(input)

    response should include ("Move(direction=")
  }
}
