import org.scalatest._

class StateSpec extends FlatSpec with Matchers {
  "constructor" should "throw an exception if command opcode is not React" in {
    val cmd = Command("Welcome", Map())
    intercept[IllegalArgumentException] {
      new State(cmd)
    }
  }

  it should "accept a command with opcode React" in {
    val cmd = Command("React", Map())
    new State(cmd)
  }

  "energy" should "return some int value if the energy key was specified" in {
    val energy = 123456
    val cmd = Command("React", Map("energy" -> energy.toString()))
    val state = new State(cmd)

    state.energy should be (Some(energy))
  }

  it should "return None if no energy key was specified" in {
    val cmd = Command("React", Map("unrealted" -> "value"))
    val state = new State(cmd)

    state.energy should be (None)
  }

  "generation" should "return Some(0) if zero was specified for the generation value" in {
    val cmd = Command("React", Map("generation" -> 0.toString()))
    val state = new State(cmd)

    state.generation should be (Some(0))
  }

  it should "return Some int value if a generation value was specified" in {
    val gen = 3
    val cmd = Command("React", Map("generation" -> gen.toString()))
    val state = new State(cmd)

    state.generation should be (Some(gen))
  }

  it should "return None if no generation key was specified" in {
    val cmd = Command("React", Map("energy" -> "10000"))
    val state = new State(cmd)

    state.generation should be (None)
  }

  "view" should "return some Vision if a view was specified" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val cmd = Command("React", Map("view" -> view.mkString))
    val state = new State(cmd)

    state.view shouldBe a [Some[Vision]]
  }
}
