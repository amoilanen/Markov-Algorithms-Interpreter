package markov

import scala.io.Source

//run --file ./src/main/resources/binary2unary.mar --input 101
object Main {

  def main(args: Array[String]) {
    val maybeFullOptions = CommandLine.parse(args)
    maybeFullOptions match {
      case Some(options) => {
        val fileName = options.getOrElse(OptionName.FILE, "unknown")
        val input = options.getOrElse(OptionName.INPUT, "unknown")
        val sourceCode = Source.fromFile(fileName).getLines
        val algorithm = Parser.parse(sourceCode)
        val evaluation = Interpreter.execute(algorithm, input)
        println(evaluation)
      }
      case None =>
    }
  }
}