import org.scalatest._

class ControlFunctionFactorySpec extends FlatSpec with Matchers {
  "create" should "return a control function" in {
    val conFunc = new ControlFunctionFactory().create

    conFunc("Test()") should be ("")
  }
}
