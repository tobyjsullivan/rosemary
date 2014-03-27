
object MissileMode extends ThoughtMode{
  val id = "missile"

  def react(state: State): Seq[Command] = {
    // Look for nearest enemy master bot
    val allEnemyMiniBots: Set[Point] = state.view.map(v => v.findAll('S')).getOrElse(Set())
    val nearestBot: Option[Point] = if(allEnemyMiniBots.size > 0) Some(Hippocampus.findClosest(Point(0,0), allEnemyMiniBots)) else None

    val enemyDistance = nearestBot.map(p => Hippocampus.distance(Point(0,0), p))

    // Explode if sufficiently close
    val cmd = enemyDistance match {
      case Some(d) if d <= Config.MissileExplosionRange => Command("Explode", Map("size" -> Config.MissileExplosionRange.toString))
      case None => Command("Set", Map("mode" -> ForageMode.id)) // Change to Forage Mode
    }

    Seq(cmd)
  }
}
