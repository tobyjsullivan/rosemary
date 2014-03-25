import org.scalatest._
import org.scalatest.matchers._

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

    assert(state.energy === Some(energy))
  }

  it should "return None if no energy key was specified" in {
    val cmd = Command("React", Map("unrealted" -> "value"))
    val state = new State(cmd)

    assert(state.energy === None)
  }

  "generation" should "return Some(0) if zero was specified for the generation value" in {
    val cmd = Command("React", Map("generation" -> 0.toString()))
    val state = new State(cmd)

    assert(state.generation === Some(0))
  }

  it should "return Some int value if a generation value was specified" in {
    val gen = 3
    val cmd = Command("React", Map("generation" -> gen.toString()))
    val state = new State(cmd)

    assert(state.generation === Some(gen))
  }

  it should "return None if no generation key was specified" in {
    val cmd = Command("React", Map("energy" -> "10000"))
    val state = new State(cmd)

    assert(state.generation === None)
  }

  "collision" should "return the correct point if a collision was specified" in {
    val cmd = Command("React", Map("collision" -> "-1:1"))
    val state = new State(cmd)

    assert(state.collision === Some(Point(-1,1)))
  }

  it should "return None if no collision key was specified" in {
    val cmd = Command("React", Map("energy" -> "10000"))
    val state = new State(cmd)

    assert(state.collision === None)
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

    assert(state.view.isInstanceOf[Some[Vision]])
  }

  "recall" should "return None for an unset key" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    assert(state.recall("doesntExist") === None)
  }

  it should "return the correct value for a remembered key" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    state.remember("keyA", "myValue")

    assert(state.recall("keyA") === Some("myValue"))
  }

  it should "return exactly the last remembered value for a given key" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    state.remember("keyA", "someValue")
    state.remember("keyA", "aNewValue")

    assert(state.recall("keyA") === Some("aNewValue"))
  }

  it should "remember multiple different values for different keys" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    state.remember("keyA", "value201")
    state.remember("keyB", "value 402")
    state.remember("keyC", "valueXYZ")


    assert(state.recall("keyA") === Some("value201"))
    assert(state.recall("keyB") === Some("value 402"))
    assert(state.recall("keyC") === Some("valueXYZ"))
  }

  it should "return the same value for the same key consistently" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    state.remember("keyA", "value1")

    assert(state.recall("keyA") === Some("value1"))
    assert(state.recall("keyA") === Some("value1"))
    assert(state.recall("keyA") === Some("value1"))
    assert(state.recall("keyA") === Some("value1"))
  }

  it should "return values specied in the react command" in {
    val cmd = Command("React", Map("generation" -> "0", "keyA" -> "value123"))
    val state = new State(cmd)

    assert(state.recall("keyA") === Some("value123"))
  }

  "memoryConsolidation" should "return an empty map if nothing has been remembered" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    assert(state.memoryConsolidation() === Command("Set", Map()))
  }

  it should "return all key-value pairs that have been remembered" in {
    val cmd = Command("React", Map("generation" -> "0"))
    val state = new State(cmd)

    state.remember("keyA", "valueA")
    state.remember("keyB", "value333")

    assert(state.memoryConsolidation() === Command("Set", Map("keyA" -> "valueA", "keyB" -> "value333")))
  }
}
