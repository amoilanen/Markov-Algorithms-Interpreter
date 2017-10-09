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

case class ContinuingRule(from: String, to: String) extends Rule {

  override def toString: String = {
    s"ContinuingRule: $from -> $to"
  }
}

case class TerminatingRule(from: String, to: String) extends Rule {

  override def toString: String = {
    s"TerminatingRule: $from -> $to"
  }
}

case class Algorithm(rules: List[Rule])

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

object Interpreter {

  def execute(algorithm: Algorithm, input: String): AlgorithmEvaluation = {

    @tailrec
    def executeNextRules(algorithm: Algorithm, input: String, steps: List[RuleEvaluation]): List[RuleEvaluation] = {
      val nextApplicableRule = algorithm.rules.find(rule => input.contains(rule.from))
      nextApplicableRule match {
        case Some(rule) => {
          val step = rule.apply(input)
          val inputAfterRuleApplication = step.after
          val updatedSteps = steps :+ step

          rule match {
            case TerminatingRule(_, _) => updatedSteps
            case ContinuingRule(_, _) => executeNextRules(algorithm, inputAfterRuleApplication, updatedSteps)
          }
        }
        case None => steps
      }
    }

    AlgorithmEvaluation(input, executeNextRules(algorithm, input, List()))
  }

}