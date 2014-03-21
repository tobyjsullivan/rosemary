package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class CommandSpec extends FlatSpec with Matchers {
  "parse" should "correctly parse a simple string" in {
    val input = "TestCommand(important=value,keyA=valueA)"

    val cmd = Command.parse(input)

    cmd.opcode should be ("TestCommand")
    cmd.params shouldEqual Map("important" -> "value", "keyA" -> "valueA")
  }

  "toString" should "output a valid command" in {
    val cmd = Command("MyCommand", Map("key1" -> "value1", "key2" -> "value2"))

    cmd.toString should be ("MyCommand(key1=value1,key2=value2)")
  }
}
