object Command {
  def parse(input: String): Command = {
    val pieces = input.split('(')
    val opcode = pieces(0)
    val params = pieces(1).dropRight(1).split(',').filter(s => s.contains('=')).map(s => {
      val parts = s.split('=')
      parts(0) -> parts(1)
    }).toMap

    Command(opcode, params)
  }

  def compose(commands: Seq[Command]): String = {
    commands.mkString("|")
  }
}

case class Command (opcode: String, params: Map[String, String]) {
  override def toString = opcode+"("+ params.map(kv => kv._1 + "=" + kv._2).mkString(",")+")"
}
