package markov

import org.scalatest.{FunSpec, Matchers}

class InterpreterSpec extends FunSpec with Matchers {

  describe("evaluation") {

    describe("simple algorithm") {

      it("should evaluate") {

        //#Changes ones to zeros
        val algorithm =
          "q1" -> "0q" &
          "q0" -> "0q" &
          "q" -> "." &
          "" -> "q"

        val evaluation = Interpreter.execute(algorithm, "111")

        val expectedEvaluation =
          ("111" to "q111" by "" -> "q") &
          ("q111" to "0q11" by "q1" -> "0q") &
          ("0q11" to "00q1" by "q1" -> "0q") &
          ("00q1" to "000q" by "q1" -> "0q") &
          ("000q" to "000" by "q" -> ".")
        evaluation should equal(expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }
    }

    describe("special characters") {

      it("should interpret dot in left rule part as a literal, not as 'any' symbol") {
        val algorithm = "." -> ""

        val evaluation = Interpreter.execute(algorithm, "1.")

        val expectedEvaluation = ("1." to "1" by "." -> "").only
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }
    }

    describe("termination") {

      it("should stop after terminating rule") {
        val algorithm =
          "1" -> "2" &
          "2" -> ".3" &
          "3" -> "4"

        val evaluation = Interpreter.execute(algorithm, "1")

        val expectedEvaluation =
          ("1" to "2" by "1" -> "2") &
          ("2" to "3" by "2" -> ".3")
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }

      it("should stop if there are no rules to apply") {
        val algorithm = "1" -> "0"
        val evaluation = Interpreter.execute(algorithm, "0")

        val expectedEvaluation = AlgorithmEvaluation("0", List())
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }

      it("should continue if there is a rule that can be applied") {
        val algorithm = "1" -> "0"
        val input = "1"
        val evaluation = Interpreter.execute(algorithm, input)

        val expectedEvaluation = ("1" to "0" by "1" -> "0").only
        assert(evaluation == expectedEvaluation)
        assert(evaluation.result == expectedEvaluation.result)
      }
    }
  }
}

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
