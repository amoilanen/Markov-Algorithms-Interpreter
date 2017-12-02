package object markov {

  implicit def String2RuleBuilder(str: String): RuleBuilder =
    RuleBuilder(str)

  implicit def String2RuleEvaluation(str: String): RuleEvaluation =
    RuleEvaluation(str, null, null)

  implicit def Rule2Algorithm(rule: Rule): Algorithm =
    Algorithm(List(rule))

  implicit def RuleEvaluation2AlgorithmEvaluation(ruleEvaluation: RuleEvaluation): AlgorithmEvaluation =
    AlgorithmEvaluation(ruleEvaluation.before, List(ruleEvaluation))
}
