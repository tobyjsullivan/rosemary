package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class ControlFunctionFactorySpec extends FlatSpec with Matchers {
  "create" should "return a control function" in {
    val conFunc = new ControlFunctionFactory().create

    conFunc("Test()") should be ("")
  }
}
