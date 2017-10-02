package markov

case class Rule(from: String, to: String, isTerminal: Boolean)
case class Algorithm(rules: List[Rule])
case class EvaluationStep(rule: Rule, before: String, after: String)
case class Evaluation(steps: List[EvaluationStep])

object Interpreter {

  def execute(algorithm: Algorithm, input: String): Evaluation = {
    ???
  }
}