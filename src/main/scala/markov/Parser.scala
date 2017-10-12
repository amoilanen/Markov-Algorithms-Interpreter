package markov

object Parser {

  def parse(algorithmCode: Iterator[String]): Algorithm = {
    val linesWithoutComments = algorithmCode.filterNot(line => line.startsWith("#"))
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

    Algorithm(rules)
  }
}