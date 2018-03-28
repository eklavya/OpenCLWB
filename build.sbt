name := "OpenCLWB"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.aparapi" % "aparapi" % "1.5.0"
)

fork in run := true

javaOptions in run ++= Seq(
  "-Xms8G",
  "-Xmx8G"
)
