package markov

import java.util.regex.Pattern

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
    s"$from -> $to"
  }
}

case class TerminatingRule(from: String, to: String) extends Rule {

  override def toString: String = {
    s"$from ->. $to (terminating)"
  }
}

case class Algorithm(rules: List[Rule])