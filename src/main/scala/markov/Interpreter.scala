package markov

trait Rule {
  def from: String
  def to: String
}

case class ContinuingRule(from: String, to: String) extends Rule

case class TerminatingRule(from: String, to: String) extends Rule

case class Algorithm(rules: List[Rule])

case class EvaluationStep(before: String, after: String, rule: Rule)

case class Evaluation(steps: List[EvaluationStep]) {
  def result(): Option[String] =
    if (steps.size > 0)
      Some(steps.last.after)
    else
      None
}

object Interpreter {

  def execute(algorithm: Algorithm, input: String): Evaluation = {
    ???
  }
}