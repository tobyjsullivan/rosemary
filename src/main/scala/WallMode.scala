
object WallMode extends ThoughtMode {
  val id = "wall"

  def react(state: State): Seq[Command] = {
    val relPos: Point = (state.recall("RelPos"), state.collision) match {
      case (Some(sPos), Some(collision)) => {
        val pos = Point.parse(sPos)
        pos - collision
      }
      case (Some(sPos), None) => Point.parse(sPos)
      case ( None, _) => Point(0,0)
    }

    val startPos = state.recall("wallStart").map(s => Point.parse(s)).getOrElse(Point(0,0))
    val targetPos = state.recall("wallTarget").map(s => Point.parse(s)).getOrElse(Point(0,0))
    val heading = state.recall("wallHeading").map(s => Point.parse(s)).getOrElse(Point(0,0))

    val startDist = Hippocampus.distance(startPos, targetPos)
    val curDist = Hippocampus.distance(relPos, targetPos)

    val bestDir: Point = if(curDist < startDist) {
      // Switch back to forage mode
      state.remember("mode", ForageMode.id)
      Point(0, 0) // Don't move
    } else if(state.view.map(v => v.lookAt(heading)).getOrElse('?') == 'W') {
      // If a wall is ahead, turn 90 deg clockwise
      val newHeading = heading match {
        case Point(0,-1) => Point(1, 0)
        case Point(0, 1) => Point(-1, 0)
        case Point(-1,0) => Point(0, -1)
        case Point(1, 0) => Point(0, 1)
        case _ => Point(0, -1)
      }
      state.remember("wallHeading", newHeading.toString)
      newHeading
    } else {
      // continue on heading
      heading
    }

    val newRelPos = relPos + bestDir
    state.remember("RelPos", newRelPos.toString)

    val move = Command("Move", Map("direction" -> bestDir.truncate.toString))

    Seq(move, state.memoryConsolidation())
  }


  def initVals(start: Point, target: Point, state: State) = {
    state.remember("mode", WallMode.id)
    state.remember("wallStart", start.toString)
    state.remember("wallTarget", target.toString)

    val heading = target.truncate match {
      case p @ (Point(0, _) | Point(_, 0)) => p
      case Point(x, _) => Point(x, 0)
    }

    state.remember("wallHeading", heading.toString)
  }

  def inCorner(state: State): Boolean = {
    val surroundingElements = for {
      x <- -1 to 1
      y <- -1 to 1 if !(x == 0 && y == 0)
      point = Point(x, y)
      c = state.view.map(v => v.lookAt(point))
    } yield c.getOrElse('?')

    surroundingElements.filter(c => c == 'W').size >= 4
  }
}
