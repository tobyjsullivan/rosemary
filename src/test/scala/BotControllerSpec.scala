import org.scalatest._

class BotControllerSpec extends FlatSpec {
  "respond" should "respond to a welcome command by setting an apocalypse date" in {
    val apocalypse = 459
    val input = "Welcome(name=some_name,apocalypse="+apocalypse+",round=9)"

    val response = BotController.respond(input)

    assert(response.indexOf("apocalypse="+apocalypse) >= 0)
  }

  it should "respond to a goodbye command with no commands" in {
    val res = BotController.respond("Goodbye(energy=12983)")

    assert(res === "")
  }

  /**
   * This requirement is only temporary. It will not hold as the bot gets smarter
   */
  it should "Move to nearby zugar" in {
    val view = List(
      "_____",
      "W____",
      "P_M__",
      "WWW__",
      "????_"
    )

    val input = Command("React", Map("view" -> view.mkString)).toString()

    val response = BotController.respond(input)

    assert(response.contains("Move(direction=-1:0)"))
  }

  it should "Move randomly if there is no nearby zugar" in {
    val view = List(
      "_____",
      "W____",
      "__M__",
      "WWW__",
      "????_"
    )

    val input = Command("React", Map("view" -> view.mkString)).toString()

    val response = BotController.respond(input)

    assert(response.contains("Move(direction="))
  }
}
