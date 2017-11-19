package markov

import org.scalatest.FunSpec
import java.util.{EmptyStackException, Stack}

import org.scalatest._

class InterpreterSpec extends FunSpec with Matchers {

  describe("Evaluation") {

    describe("Simple algorithm") {

      it("should evaluate") {

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

        val expectedEvaluation = AlgorithmEvaluation(input, List(
          RuleEvaluation("111", "q111", startAlgorithmRule),
          RuleEvaluation("q111", "0q11", changeOneToZeroRule),
          RuleEvaluation("0q11", "00q1", changeOneToZeroRule),
          RuleEvaluation("00q1", "000q", changeOneToZeroRule),
          RuleEvaluation("000q", "000", finishAlgorithmRule),
        ))

        evaluation should equal(expectedEvaluation)
      }
    }

    describe("dot symbol in the from part of the rule") {

      it("should interpret dot as a literal, not as 'any' symbol") {
        val dotRule = ContinuingRule(".", "")
        val algorithm = Algorithm(List(
          dotRule
        ))
        val input = "1."
        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = AlgorithmEvaluation(input, List(
          RuleEvaluation("1.", "1", dotRule),
        ))

        assert(evaluation == expectedEvaluation)
      }
    }

    describe("no rules apply") {

      it("should have not evaluation steps") {
        val replaceOnesRule = ContinuingRule("1", "0")
        val algorithm = Algorithm(List(
          replaceOnesRule
        ))
        val input = "0"
        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = AlgorithmEvaluation(input, List())

        assert(evaluation == expectedEvaluation)
      }
    }
  }
}

  //TODO: Algorithm terminates after a terminating rule
  //TODO: Algorithm does not terminate after a non-terminating rule
  //TODO: Order or rules should matter, rules are applied in the order of definition
  //TODO: Only one and the first occurrence of the rule's left part is substituted
  //TODO: No rules provided
  //TODO: What happens with a non-terminating algorithm?
  //TODO: '>' is not an allowed symbol

  //TODO: Add tests for the parser
  //TODO: Add tests for the  Markov algorithm examples

  //TODO: REPL mode, algorithm is input as alg="q1>0q >> q0>0q >> q>. >> >q"
  //Then REPL prints alg=Algorithm(...)
  //Then alg 111 would print all the evaluation steps and the final result
  //TODO: File evaluation mode: will load an algorithm in the provided file and apply it against the provided input

  //TODO: Package the license, examples, JAR file and Shell script in a nice way like
