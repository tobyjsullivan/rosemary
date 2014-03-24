import org.scalatest._

class PointSpec extends FlatSpec {
  "toDirectionString" should "trim directions to acceptable direction range" in {
    assert(Point(5, -3).toDirectionString === "1:-1")
    assert(Point(-3, 0).toDirectionString === "-1:0")
    assert(Point(0,0).toDirectionString === "0:0")
    assert(Point(-4,-8).toDirectionString === "-1:-1")
    assert(Point(6, 4).toDirectionString === "1:1")
    assert(Point(1, -5).toDirectionString === "1:-1")
  }
}
