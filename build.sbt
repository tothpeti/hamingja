ThisBuild / scalaVersion := "2.13.10"

version := "1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"            % "2.9.0",
  "org.typelevel" %% "cats-effect"          % "3.4.1",
  "co.fs2"        %% "fs2-core"             % "3.4.0",
  "co.fs2"        %% "fs2-io"               % "3.4.0",
  "org.gnieh"     %% "fs2-data-csv"         % "1.6.0",
  "org.gnieh"     %% "fs2-data-csv-generic" % "1.6.0",
  "org.http4s"    %% "http4s-dsl"           % "0.23.16",
  "org.http4s"    %% "http4s-ember-client"  % "0.23.16",
  "com.monovore"  %% "decline-effect"       % "2.4.0",
  compilerPlugin("com.github.ghik" %% "zerowaste" % "0.2.1" cross CrossVersion.full)
)

scalacOptions ++= Seq(
  "-Xfatal-warnings"
)

lazy val root = (project in file("."))
  .settings(
    name := "hamingja"
  )
