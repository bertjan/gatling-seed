package nl.jpoint.gatling

import java.nio.file.Path

import io.gatling.core.util.PathHelper._

object PathHelper {

	val recorderConfUrl: Path = getClass.getClassLoader.getResource("config/recorder.conf").toURI
	val projectRootDir = recorderConfUrl.ancestor(4)
	val mavenTargetDirectory = projectRootDir / "target"
	val recorderOutputPath = mavenTargetDirectory / "recorder-output"
	val simulationOutputFolder = (recorderOutputPath / "simulations").toString
	val simulationRequestBodiesFolder = (recorderOutputPath / "bodies").toString
	val mavenSourcesDirectory = projectRootDir / "src" / "main" / "scala"
  val mavenTestSourcesDirectory = projectRootDir / "src" / "test" / "scala"
	val mavenResourcesDirectory = projectRootDir / "src" / "main" / "resources"
	val mavenTestResourcesDirectory = projectRootDir / "src" / "test" / "resources"
	val harInputDirectory = (mavenResourcesDirectory / "har" ).toString
	val gatlingImportConfig = (mavenResourcesDirectory / "config" / "gatling-recorder.json").toString
  val simulationTargetFolder = (mavenTestSourcesDirectory).toString
  val requestBodiesTargetFolder = (mavenTestResourcesDirectory / "request-bodies").toString

}
