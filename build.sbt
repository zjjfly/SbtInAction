import scala.sys.process.Process

name := "SbtInAction"

version := "0.1"

scalaVersion := "2.12.7"

val gitHeadCommitSha = taskKey[String]("Determines the current git commit SHA")
gitHeadCommitSha := Process ("git rev-parse HEAD").lines.head
