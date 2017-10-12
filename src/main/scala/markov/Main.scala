package markov

import scala.io.Source

object Main {

  def main(args: Array[String]) {
    val input = "101" //5

    val lines = Source.fromResource("binary2unary.mar").getLines
    val algorithm = Parser.parse(lines)
    val evaluation = Interpreter.execute(algorithm, input)

    println(evaluation)
  }
}