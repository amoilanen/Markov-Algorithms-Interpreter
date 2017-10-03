package markov

import collection.mutable.Stack
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

    val expectedEvaluation = Evaluation(List(
      EvaluationStep("111", "q111", startAlgorithmRule),
      EvaluationStep("q111", "0q11", changeOneToZeroRule),
      EvaluationStep("0q11", "00q1", changeOneToZeroRule),
      EvaluationStep("00q1", "000q", changeOneToZeroRule),
      EvaluationStep("000q", "000", finishAlgorithmRule),
    ))

    assert(evaluation == expectedEvaluation)
  }

  //TODO: Order or rules should matter, rules are applied in the order of definition
  //TODO: No rules provided
  //TODO: What happens with a non-terminating algorithm?
}
