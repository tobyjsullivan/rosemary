import org.scalatest._

class PointSpec extends FlatSpec {
  "toString" should "return a proper formatted direction string" in {
    assert(Point(5, -3).toString === "5:-3")
    assert(Point(-3, 0).toString === "-3:0")
    assert(Point(0,0).toString === "0:0")
    assert(Point(-4,-8).toString === "-4:-8")
    assert(Point(6, 4).toString === "6:4")
    assert(Point(1, -5).toString === "1:-5")
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

  "parse" should "throw an exception when an invalid string is provided" in {
    intercept[IllegalArgumentException] {
      Point.parse("not a point")
    }

    intercept[IllegalArgumentException] {
      Point.parse(":")
    }
  }

  it should "correctly parse valid coordinates" in {
    assert(Point.parse("-1:1") === Point(-1, 1))
    assert(Point.parse("13:-21") === Point(13, -21))
    assert(Point.parse("0:0") === Point(0, 0))
  }

  "operator-" should "correctly subtract vector" in {
    assert(Point(0, 0) - Point(3, 2) === Point(-3, -2))
    assert(Point(0, 0) - Point(-3, 2) === Point(3, -2))
    assert(Point(0, 0) - Point(3, -2) === Point(-3, 2))
    assert(Point(-10, 10) - Point(5, 7) === Point(-15, 3))
  }

  "operator+" should "correctly sum vector" in {
    assert(Point(0, 0) + Point(3, 2) === Point(3, 2))
    assert(Point(0, 0) + Point(-3, 2) === Point(-3, 2))
    assert(Point(0, 0) + Point(3, -2) === Point(3, -2))
    assert(Point(-10, 10) + Point(5, 7) === Point(-5, 17))
  }
}
