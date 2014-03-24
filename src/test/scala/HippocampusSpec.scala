import org.scalatest._

class HippocampusSpec extends FlatSpec {
  "relPosition" should "return the correct vector between two points" in {

    val res1 = Hippocampus.relPosition(Point(3, 4), Point(-2, 1))
    assert(res1 === Point(-5, -3))


    val res2 = Hippocampus.relPosition(Point(-2, 1), Point(3, 4))
    assert(res2 === Point(5, 3))
  }

  "distance" should "return the direct distance between two points within an acceptable amount" in {
    val res = Hippocampus.distance(Point(3, 4), Point(-2, 1))

    // Allow for a tolerance (rounding, whatevs)
    assert(res > 5.830950)
    assert(res < 5.830952)
  }

  "findClosest" should "return the point in the set closest to the origin" in {
    val origin = Point(0, 0)

    val points = Set(Point(3, 5), Point(-2, -3), Point(5, -3), Point(-5, 1))

    val res = Hippocampus.findClosest(origin, points)
    assert(res === Point(-2, -3))
  }

  "findClosest" should "throw an exception when an empty set is provided" in {
    intercept[IllegalArgumentException] {
      Hippocampus.findClosest(Point(0, 0), Set())
    }
  }
}
