package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class VisionSpec extends FlatSpec with Matchers {
  "constructor" should "throw an exception if length of input is not a perfect square" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "???"
    )

    intercept[IllegalArgumentException] {
      new Vision(view.mkString)
    }
  }

  it should "throw an exception if the view contains any invalid characters" in {
    val view = List(
      "__s_p",
      "W_L_b", // There is an L on this line
      "P_MS_",
      "WWW_B",
      "????_"
    )

    intercept[IllegalArgumentException] {
      new Vision(view.mkString)
    }
  }

  it should "not throw an exception if the input is valid" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    new Vision(view.mkString)
  }

  "size" should "be equal to 5 for a 5x5 input" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    vision.size should be (5)
  }

  "center" should "be 3,3 for an input of 5x5" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    vision.center should equal (Point(2, 2))
  }

  "range" should "be 2 for an input of 5x5" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    vision.range should equal (2)
  }

  "lookAt" should "return the correct value for a given position within view" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    vision.lookAt(Point(2, -1)) should equal ('b')
    vision.lookAt(Point(0, 0)) should equal ('M')
    vision.lookAt(Point(-1, 1)) should equal ('W')
    vision.lookAt(Point(0, -2)) should equal ('s')
  }

  it should "throw an exception for a position that is outside of view" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    intercept[IllegalArgumentException]{
      vision.lookAt(Point(-3, 0))
    }
    intercept[IllegalArgumentException]{
      vision.lookAt(Point(0, -3))
    }
    intercept[IllegalArgumentException]{
      vision.lookAt(Point(3, 0))
    }
    intercept[IllegalArgumentException]{
      vision.lookAt(Point(0, 3))
    }


  }
}
