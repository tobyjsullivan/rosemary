package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class HippocampusSpec extends FlatSpec with Matchers {
  "relPosition" should "return the correct vector between two points" in {
    Hippocampus.relPosition(Point(3, 4), Point(-2, 1)) should be (Point(-5, -3))
    Hippocampus.relPosition(Point(-2, 1), Point(3, 4)) should be (Point(5, 3))
  }

  "distance" should "return the direct distance between two points within an acceptable amount" in {
    val beWithinTolerance = be > 5.830950 and be < 5.830952

    Hippocampus.distance(Point(3, 4), Point(-2, 1)) should beWithinTolerance
  }

  "findClosest" should "return the point in the set closest to the origin" in {
    val origin = Point(0, 0)

    val points = Set(Point(3, 5), Point(-2, -3), Point(5, -3), Point(-5, 1))

    Hippocampus.findClosest(origin, points) should be (Point(-2, -3))
  }

  "findClosest" should "throw an exception when an empty set is provided" in {
    intercept[IllegalArgumentException] {
      Hippocampus.findClosest(Point(0, 0), Set())
    }
  }
}
