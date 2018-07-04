val common = Seq(
  name := "MarkovAlgorithmInterpreter",
  version := "0.1",
  scalaVersion := "2.12.6",
  exportJars := true,
  mainClass := Some("markov.Main"),
  libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

val dist = TaskKey[Unit]("dist", "Builds a redistributable package")
val distTask = dist := {
  val examples = (Keys.sourceDirectory.value / "main/resources/") ** "*.mar"
  val launchScript = Keys.sourceDirectory.value / "main/resources/mrkv.sh"
  val license = Keys.baseDirectory.value / "LICENSE"
  val readme = Keys.baseDirectory.value / "README.md"
  val jar = (Keys.packageBin in Compile).value
  val targetDir = Keys.target.value
  val distDir = targetDir / "markov-algorithms"
  val distDirExamples = distDir / "examples"

  examples.get.foreach(example => {
    val exampleShortName = example.getName
    IO.copyFile(example, distDirExamples / exampleShortName)
  })
  IO.copyFile(jar, distDir / s"${Keys.name.value}.jar")
  IO.copyFile(license, distDir / "LICENSE")
  IO.copyFile(readme, distDir / "README.md")
  IO.copyFile(launchScript, distDir / "mrkv.sh")

  val allDistributedFiles: Seq[File] = (distDir ** "*").get

  val distributedFiles = allDistributedFiles.map(file => {
    val filePath = file.getPath
    val pathRelativeToDistDir = filePath.substring(distDir.getPath.length)
    (file, pathRelativeToDistDir)
  })
  print(distributedFiles)
  IO.zip(distributedFiles, targetDir / s"${Keys.name.value}.zip")
}

lazy val project = Project(
  "MarkovAlgorithmsInterpreter",
  file(".")
).settings(common, distTask)