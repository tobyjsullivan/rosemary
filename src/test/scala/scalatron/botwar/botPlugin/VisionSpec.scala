package scalatron.botwar.botPlugin

import org.scalatest._
import org.scalatest.Matchers._

class VisionSpec extends FlatSpec with Matchers {
  "constructor" should "throw an exception if length of input is not a perfect square" in {
    val view = Set(
      "__S_p",
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
    val view = Set(
      "__S_p",
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
    val view = Set(
      "__S_p",
      "W___b",
      "P_MS_",
      "WWW_B",
      "????_"
    )

    new Vision(view.mkString)
  }
}
