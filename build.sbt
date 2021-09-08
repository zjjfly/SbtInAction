import scala.sys.process.Process

organization := "com.github.zjjfly"

name := "SbtInAction"

version := "0.1"

scalaVersion := "2.12.7"

enablePlugins(GraalVMNativeImagePlugin)

mainClass in Compile := Some("com.github.zjjfly.sia.Main")

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.8.3" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

//初始化一个task对象,类型是TaskKey
val gitHeadCommitSha: TaskKey[String] = taskKey[String]("Determines the current git commit SHA")
//定义task的如何执行,使用:=
gitHeadCommitSha := Process("git rev-parse HEAD").lineStream.head

//定义一个依赖gitHeadCommitSha的task
//Seq[File]是任务的返回值类型
val makeVersionProperties = taskKey[Seq[File]]("Makes a version.properties file.")
makeVersionProperties := {
  val propFile = new File((resourceManaged in Compile).value, "version.properties")
  //通过task的value方法获取它的执行结果
  val content = s"version=${gitHeadCommitSha.value}"
  IO.write(propFile, content)
  Seq(propFile)
}

//sbt的task是可以并行的
val taskA = taskKey[String]("taskA")
val taskB = taskKey[String]("taskB")
val taskC = taskKey[String]("taskC")
taskA := {
  val b = taskB.value;
  val c = taskC.value;
  "taskA"
}
taskB := {
  Thread.sleep(5000);
  "taskB"
}
taskC := {
  Thread.sleep(5000);
  "taskC"
}
//如果是串行的,那么taskA的执行时间至少需要10s,但实际只要5s

//把makeVersionProperties产生的文件加入编译生成的resources中
//resourceGenerators是存放了一个Task列表的SettingKey
//Compile是一种Configuration,类似其他Maven中scope,相同的SettingKey在不同的Configuration中有不同的含义
//常用的Configuration有Compile和Test
resourceGenerators in Compile += makeVersionProperties

//定义子项目
//使用lazy是为了解决项目之间可能出现的循环引用
lazy val common = (
  Project("common", file("common")).
    settings (
    libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.8.3" % "test")
    )
  )

lazy val analytics = (
  Project("analytics", file("analytics")).
    dependsOn (common).
    settings()
  )

lazy val website = (
  Project("website", file("website")).
    dependsOn (common).
    settings()
  )
