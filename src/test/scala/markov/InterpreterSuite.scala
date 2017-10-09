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

  test("dot is interpreted as a literal, not as 'any' symbol") {
    val dotRule = ContinuingRule(".", "")
    val algorithm = Algorithm(List(
      dotRule
    ))
    val input = "1."
    val evaluation = Interpreter.execute(algorithm, input)

    val expectedEvaluation = AlgorithmEvaluation(List(
      RuleEvaluation("1.", "1", dotRule),
    ))

    assert(evaluation == expectedEvaluation)
  }

  test("if no rules to apply, there are no evaluation steps") {
    val replaceOnesRule = ContinuingRule("1", "0")
    val algorithm = Algorithm(List(
      replaceOnesRule
    ))
    val input = "0"
    val evaluation = Interpreter.execute(algorithm, input)

    val expectedEvaluation = AlgorithmEvaluation(List())

    assert(evaluation == expectedEvaluation)
  }

  //TODO: Read provided algorithm file from the file system and evaluate it

  //TODO: Algorithm terminates after a terminating rule
  //TODO: Algorithm does not terminate after a non-terminating rule
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
