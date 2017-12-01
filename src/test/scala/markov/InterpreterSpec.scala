package markov

import org.scalatest.{FunSpec, Matchers}

class InterpreterSpec extends FunSpec with Matchers {

  describe("evaluation") {

    describe("simple algorithm") {

      it("should evaluate") {

        //#Changes ones to zeros
        //q1>0q
        //q0>0q
        //q>.
        //>q
        val changeOneToZeroRule = Rule("q1", "0q")
        val leaveZeroInPlaceRule = Rule("q0", "0q")
        val finishAlgorithmRule = Rule("q", ".")
        val startAlgorithmRule = Rule("", "q")
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

    describe("special characters") {

      it("should interpret dot in left rule part as a literal, not as 'any' symbol") {
        val dotRule = Rule(".", "")
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

    describe("termination") {

      it("should stop after terminating rule") {
        val replaceOneWithTwo = Rule("1", "2")
        val replaceTwoWithThree = Rule("2", ".3")
        val replaceThreeWithFour = Rule("3", "4")
        val algorithm = Algorithm(List(
          replaceOneWithTwo,
          replaceTwoWithThree,
          replaceThreeWithFour
        ))
        val input = "1"

        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = AlgorithmEvaluation(input, List(
          RuleEvaluation("1", "2", replaceOneWithTwo),
          RuleEvaluation("2", "3", replaceTwoWithThree)
        ))
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }

      it("should stop if there are no rules to apply") {
        val replaceOnesRule = Rule("1", "0")
        val algorithm = Algorithm(List(
          replaceOnesRule
        ))
        val input = "0"
        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = AlgorithmEvaluation(input, List())
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }

      it("should continue if there is a rule that can be applied") {
        val replaceOnesRule = Rule("1", "0")
        val algorithm = Algorithm(List(
          replaceOnesRule
        ))
        val input = "1"
        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = AlgorithmEvaluation(input, List(
          RuleEvaluation("1", "0", replaceOnesRule)
        ))
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }
    }
  }
}

  //TODO: DSL for defining algorithms and rules?

  //TODO: Order or rules should matter, rules are applied in the order of definition
  //TODO: Only one and the first occurrence of the rule's left part is substituted
  //TODO: No rules provided
  //TODO: What happens with a non-terminating algorithm?

  //TODO: '>' is not an allowed symbol

  //TODO: Add tests for the parser
  //TODO: Add tests for the interpreter itself?
  //TODO: Add tests for command line parsing
  //TODO: Add tests for the  Markov algorithm examples

  //TODO: REPL mode, algorithm is input as alg="q1>0q >> q0>0q >> q>. >> >q"
  //Then REPL prints alg=Algorithm(...)
  //Then alg 111 would print all the evaluation steps and the final result
  //TODO: File evaluation mode: will load an algorithm in the provided file and apply it against the provided input

  //TODO: Package the license, examples, JAR file and Shell script in a nice way like
