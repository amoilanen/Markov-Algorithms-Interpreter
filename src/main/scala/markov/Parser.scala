package markov

object Parser {

  def parse(algorithmCode: Iterator[String]): Algorithm = {
    val linesWithoutComments = algorithmCode.filterNot(line => line.startsWith("#"))
    val rules = linesWithoutComments.map(line => {
      val Array(left, right) = (line.split(">") ++ Array("", "")).take(2)
      Rule(left, right)
    }).toList

    Algorithm(rules)
  }
}