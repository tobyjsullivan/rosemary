package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class ControlFunctionFactorySpec extends FlatSpec with Matchers {
  "create" should "return a control function" in {
    val conFunc = ControlFunctionFactory.create

    conFunc("Test()") should be ("")
  }
}
