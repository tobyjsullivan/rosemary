package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

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
}
