import org.scalatest._

class PointSpec extends FlatSpec with Matchers {
  "toDirectionString" should "trim directions to acceptable direction range" in {
    Point(5, -3).toDirectionString shouldEqual ("1:-1")
    Point(-3, 0).toDirectionString shouldEqual ("-1:0")
    Point(0,0).toDirectionString shouldEqual ("0:0")
    Point(-4,-8).toDirectionString shouldEqual ("-1:-1")
    Point(6, 4).toDirectionString shouldEqual ("1:1")
    Point(1, -5).toDirectionString shouldEqual("1:-1")
  }
}
