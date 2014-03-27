
object InterceptMode extends ThoughtMode {
  val id = "intercept"

  def react(state: State): Seq[Command] = {
    // Look for nearest enemy minibot
    val allEnemyMiniBots: Set[Point] = state.view.map(v => v.findAll('s')).getOrElse(Set())
    val nearestBot: Option[Point] = if(allEnemyMiniBots.size > 0) Some(Hippocampus.findClosest(Point(0,0), allEnemyMiniBots)) else None

    val enemyDistance = nearestBot.map(p => Hippocampus.distance(Point(0,0), p))

    val curExplosionDamage = enemyDistance.map(d => damageToEnemy(d, state.energy.getOrElse(0)))

    // Explode if sufficiently close
    val cmd = curExplosionDamage match {
      case Some(d) if d >= Config.InterceptBotDamage => Command("Explode", Map("size" -> math.ceil(enemyDistance.get).toInt.toString))
      case Some(_) => Command("Move", Map("direction" -> nearestBot.get.truncate.toString))
      case None => Command("Set", Map("mode" -> ForageMode.id)) // Change to Forage Mode
    }

    Seq(cmd)
  }

  private def damageToEnemy(enemyDist: Double, energy: Int): Int = {
    val radius = math.ceil(enemyDist)
    val blastArea = radius * radius * math.Pi
    val energyPerArea = energy / blastArea

    val damageAtCentre = energyPerArea * 200.0

    (damageAtCentre * (1 - (enemyDist / radius))).toInt
  }
}
