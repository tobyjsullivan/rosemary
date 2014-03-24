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

  "truncate" should "trim point to acceptable direction range" in {
    assert(Point(5, -3).truncate === Point(1, -1))
    assert(Point(-3, 0).truncate === Point(-1, 0))
    assert(Point(1, 6).truncate === Point(1, 1))
  }

  "invert" should "swap the signs on both axis" in {
    assert(Point(5, -3).invert === Point(-5, 3))
    assert(Point(0, 0).invert === Point(0,0))
    assert(Point(-2, 3).invert === Point(2, -3))
  }
}
