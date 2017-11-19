package markov

import scala.annotation.tailrec

object OptionName extends Enumeration {
  type OptionName = Value
  val FILE, INPUT = Value
}

object CommandLine {

  import OptionName._

  val usage =
    """
  Markov Algorithm interpreter. https://en.wikipedia.org/wiki/Markov_algorithm

  Usage:
    markov [--file source_file] [--input input] [--help]
"""

  type Options = Map[OptionName, String]

  @tailrec
  private[this] def parseOptions(arguments: List[String], parsed: Options = Map()): Options = arguments match {
    case "--file" :: fileName :: rest =>
      parseOptions(rest, parsed ++ Map(FILE -> fileName))
    case "--input" :: input :: rest =>
      parseOptions(rest, parsed ++ Map(INPUT -> input))
    case _ :: rest =>
      parseOptions(rest, parsed)
    case _ =>
      parsed
  }

  def parse(args: Array[String]): Option[Options] = {
    val arguments = args.toList

    val noArgumentsProvided = arguments.size == 0
    val helpRequested = arguments.exists(_ == "--help")

    if (helpRequested || noArgumentsProvided) {
      println(usage)
      None
    } else {
      val options = parseOptions(arguments)
      val file = options.get(FILE)
      val input = options.get(INPUT)

      if (file.isEmpty || input.isEmpty) {
        println(usage)
        None
      } else {
        Some(options)
      }
    }
  }
}
