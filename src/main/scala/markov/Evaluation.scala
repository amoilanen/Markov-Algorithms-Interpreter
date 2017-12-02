package markov

case class RuleEvaluation(before: String, after: String, rule: Rule) {

  def only(): AlgorithmEvaluation =
    AlgorithmEvaluation(before, List(this))

  def to(after: String) =
    this.copy(after = after)

  def by(rule: Rule) =
    this.copy(rule = rule)

  override def toString: String = {
    s"$before -> $after using rule $rule"
  }
}

case class AlgorithmEvaluation(input: String, steps: List[RuleEvaluation]) {

  def &(ruleEvaluation: RuleEvaluation): AlgorithmEvaluation =
    this.copy(steps = steps :+ ruleEvaluation)

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