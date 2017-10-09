package markov

import scala.io.Source

object Main {

  def main(args: Array[String]) {
    val input = "101" //5

    val lines = Source.fromResource("binary2unary.mar").getLines
    val linesWithoutComments = lines.filterNot(line => line.startsWith("#"))
    val rules = linesWithoutComments.map(line => {
      val Array(left, fullRight) = (line.split(">") ++ Array("", "")).take(2)
      val isTerminating = fullRight.contains(".")
      val right = fullRight.replace(".", "")
      if (isTerminating) {
        TerminatingRule(left, right)
      } else {
        ContinuingRule(left, right)
      }
    }).toList

    val algorithm = Algorithm(rules)
    val evaluation = Interpreter.execute(algorithm, input)

    println(evaluation)
  }
}