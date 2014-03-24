import org.scalatest._

class ControlFunctionFactorySpec extends FlatSpec {
  "create" should "return a control function" in {
    val conFunc = new ControlFunctionFactory().create

    assert(conFunc("Test()") === "")
  }
}
