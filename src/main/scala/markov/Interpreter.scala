package markov

import scala.annotation.tailrec

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

    @tailrec
    def executeNextRules(algorithm: Algorithm, input: String, steps: List[EvaluationStep]): Evaluation = {
      val nextApplicableRule = algorithm.rules.find(rule => input.contains(rule.from))
      nextApplicableRule match {
        case Some(rule) => {
          val inputAfterRuleApplication = input.replaceFirst(rule.from, rule.to)
          val step = EvaluationStep(input, inputAfterRuleApplication, rule)
          val updatedSteps = steps :+ step

          rule match {
            case TerminatingRule(_, _) => Evaluation(updatedSteps)
            case ContinuingRule(_, _) => executeNextRules(algorithm, inputAfterRuleApplication, updatedSteps)
          }
        }
        case None => Evaluation(steps)
      }
    }

    executeNextRules(algorithm, input, List())
  }

}