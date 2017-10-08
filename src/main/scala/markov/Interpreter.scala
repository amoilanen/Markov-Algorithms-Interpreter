package markov

import java.util.regex.Pattern

import scala.annotation.tailrec

trait Rule {
  def from: String
  def to: String
  def apply(inputBefore: String): RuleEvaluation = {
    val inputAfter = inputBefore.replaceFirst(Pattern.quote(from), to)

    RuleEvaluation(inputBefore, inputAfter, this)
  }
}

case class ContinuingRule(from: String, to: String) extends Rule

case class TerminatingRule(from: String, to: String) extends Rule

case class Algorithm(rules: List[Rule])

case class RuleEvaluation(before: String, after: String, rule: Rule)

case class AlgorithmEvaluation(steps: List[RuleEvaluation]) {
  def result(): Option[String] =
    if (steps.size > 0)
      Some(steps.last.after)
    else
      None
}

object Interpreter {

  def execute(algorithm: Algorithm, input: String): AlgorithmEvaluation = {

    @tailrec
    def executeNextRules(algorithm: Algorithm, input: String, steps: List[RuleEvaluation]): AlgorithmEvaluation = {
      val nextApplicableRule = algorithm.rules.find(rule => input.contains(rule.from))
      nextApplicableRule match {
        case Some(rule) => {
          val step = rule.apply(input)
          val inputAfterRuleApplication = step.after
          val updatedSteps = steps :+ step

          rule match {
            case TerminatingRule(_, _) => AlgorithmEvaluation(updatedSteps)
            case ContinuingRule(_, _) => executeNextRules(algorithm, inputAfterRuleApplication, updatedSteps)
          }
        }
        case None => AlgorithmEvaluation(steps)
      }
    }

    executeNextRules(algorithm, input, List())
  }

}