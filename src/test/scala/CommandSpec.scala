import org.scalatest._

class CommandSpec extends FlatSpec {
  "parse" should "correctly parse a simple string" in {
    val input = "TestCommand(important=value,keyA=valueA)"

    val cmd = Command.parse(input)

    assert(cmd.opcode === "TestCommand")
    assert(cmd.params === Map("important" -> "value", "keyA" -> "valueA"))
  }

  "toString" should "output a valid command" in {
    val cmd = Command("MyCommand", Map("key1" -> "value1", "key2" -> "value2"))

    assert(cmd.toString === "MyCommand(key1=value1,key2=value2)")
  }

  "compose" should "output correctly appended commands" in {
    val cmds = Seq(
      Command("Move", Map("direction"->"1:-1")),
      Command("Set", Map("keyA" -> "value1", "keyB" -> "value2"))
    )

    val expected = "Move(direction=1:-1)|Set(keyA=value1,keyB=value2)"

    assert(Command.compose(cmds) === expected)
  }
}
