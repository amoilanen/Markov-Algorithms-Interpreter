package markov

case class RuleEvaluation(before: String, after: String, rule: Rule) {

  override def toString: String = {
    s"$before -> $after using rule $rule"
  }
}

case class AlgorithmEvaluation(input: String, steps: List[RuleEvaluation]) {
  def result: Option[String] =
    if (steps.size > 0)
      Some(steps.last.after)
    else
      None

  override def toString: String = {
    val evaluation = steps.zipWithIndex.map({
      case (step, idx) => s"$idx: $step"
    }).mkString("\n")

    result match {
      case Some(output) => s"""Input: $input\n
Evaluation: \n$evaluation\n
Result: $output"""
      case None => ""
    }
  }
}