package markov

import scala.annotation.tailrec

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