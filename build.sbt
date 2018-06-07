name := "OpenCLWB"

version := "0.1"

scalaVersion := "2.12.6"

//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.mavenLocal

scalacOptions += "-g:vars"

libraryDependencies ++= Seq(
  "com.aparapi" % "aparapi" % "1.10.0-SNAPSHOT",
  "com.aparapi" % "aparapi-jni" % "1.2.0"
)

fork in run := true

javaOptions in run ++= Seq(
  "-Xms8G",
  "-Xmx8G",
  "-Dcom.aparapi.enableShowGeneratedOpenCL=true"
)
