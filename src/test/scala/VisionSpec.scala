import org.scalatest._

class VisionSpec extends FlatSpec {
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

    assert(vision.size === 5)
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

    assert(vision.center === Point(2, 2))
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

    assert(vision.range === 2)
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

    assert(vision.lookAt(Point(2, -1)) === 'b')
    assert(vision.lookAt(Point(0, 0)) === 'M')
    assert(vision.lookAt(Point(-1, 1)) === 'W')
    assert(vision.lookAt(Point(0, -2)) === 's')
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

  "findAll" should "throw an exception if an invalid character is specified" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    intercept[IllegalArgumentException]{
      vision.findAll('x')
    }
  }

  it should "return an empty set if the specified value is not within view" in {
    val view = List(
      "__s_p",
      "W___b",
      "__MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    assert(vision.findAll('P') === Set())
  }

  it should "return a set of a single point if exactly one instance is within view" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    assert(vision.findAll('P') === Set(Point(-2, 0)))
  }

  it should "return all points where the target is located" in {
    val view = List(
      "__s_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    val vision = new Vision(view.mkString)

    val res = vision.findAll('W')

    val exp = Set(
      Point(-2, -1),
      Point(-2, 1),
      Point(-1, 1),
      Point(0, 1)
    )

    assert(res === exp)
  }
}
