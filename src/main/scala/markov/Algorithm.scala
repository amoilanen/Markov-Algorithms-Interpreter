package markov

import java.util.regex.Pattern

trait Rule {
  def from: String
  def to: String

  def isApplicable(input: String) =
    input.contains(from)

  def evaluate(input: String): RuleEvaluation = {
    val inputAfter = input.replaceFirst(Pattern.quote(from), to)

    RuleEvaluation(input, inputAfter, this)
  }
}

case class ContinuingRule(from: String, to: String) extends Rule {

  override def toString: String = {
    s"$from -> $to"
  }
}

case class TerminatingRule(from: String, to: String) extends Rule {

  override def toString: String = {
    s"$from ->. $to (terminating)"
  }
}

object Rule {
  def apply(left: String, right: String): Rule = {
    val isTerminating = right.contains(".")
    val from = left
    val to = right.replace(".", "")
    if (isTerminating) {
      TerminatingRule(from, to)
    } else {
      ContinuingRule(from, to)
    }
  }
}

case class Algorithm(rules: List[Rule])