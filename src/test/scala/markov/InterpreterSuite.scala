package markov

import org.scalatest.FunSuite

class InterpreterSpec extends FunSuite {

  test("evaluate simple algorithm") {

    //#Changes ones to zeros
    //q1>0q
    //q0>0q
    //q>.
    //>q
    val changeOneToZeroRule = ContinuingRule("q1", "0q")
    val leaveZeroInPlaceRule = ContinuingRule("q0", "0q")
    val finishAlgorithmRule = TerminatingRule("q", "")
    val startAlgorithmRule = ContinuingRule("", "q")
    val changeOnesToZeros = Algorithm(List(
      changeOneToZeroRule,
      leaveZeroInPlaceRule,
      finishAlgorithmRule,
      startAlgorithmRule
    ))

    val input = "111"
    val evaluation = Interpreter.execute(changeOnesToZeros, input)

    val expectedEvaluation = AlgorithmEvaluation(List(
      RuleEvaluation("111", "q111", startAlgorithmRule),
      RuleEvaluation("q111", "0q11", changeOneToZeroRule),
      RuleEvaluation("0q11", "00q1", changeOneToZeroRule),
      RuleEvaluation("00q1", "000q", changeOneToZeroRule),
      RuleEvaluation("000q", "000", finishAlgorithmRule),
    ))

    assert(evaluation == expectedEvaluation)
  }

  //TODO: Test that . is interpreted as a dot symbol, not as any symbol
  //TODO: Order or rules should matter, rules are applied in the order of definition
  //TODO: Only one and the first occurrence of the rule's left part is substituted
  //TODO: No rules provided
  //TODO: What happens with a non-terminating algorithm?
  //TODO: '>' is not an allowed symbol

  //TODO: REPL mode, algorithm is input as alg="q1>0q >> q0>0q >> q>. >> >q"
  //Then REPL prints alg=Algorithm(...)
  //Then alg 111 would print all the evaluation steps and the final result
  //TODO: File evaluation mode: will load an algorithm in the provided file and apply it against the provided input
}
