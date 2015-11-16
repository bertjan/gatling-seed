package nl.jpoint.gatling

import java.io.File
import java.nio.file.{Paths, Files}
import java.util

import io.gatling.recorder.GatlingRecorder
import io.gatling.recorder.config.{RecorderMode, RecorderPropertiesBuilder}
import org.apache.commons.io.FileUtils

import scala.util.parsing.json.JSON

object GatlingRecorderWrapper extends App {

  def startRecorder(props: RecorderPropertiesBuilder, simulationClassName : String) {

    println("Using project root dir " + PathHelper.projectRootDir)

    props.simulationOutputFolder(PathHelper.simulationOutputFolder)
    props.bodiesFolder(PathHelper.simulationRequestBodiesFolder)
    props.simulationClassName(simulationClassName)
    props.simulationPackage("simulations")
    props.mode(RecorderMode.Har)
    props.headless(true)
    props.automaticReferer(true)
    props.checkResponseBodies(false)
    props.followRedirect(true)
    props.inferHtmlResources(true)
    props.removeCacheHeaders(true)
    props.encoding("utf-8")
    props.thresholdForPauseCreation("100")
    props.filterStrategy("BlacklistFirst")
    props.blacklist(util.Arrays.asList(
      ".*\\.js",
      ".*\\.css",
      ".*\\.gif",
      ".*\\.jpeg",
      ".*\\.jpg",
      ".*\\.ico",
      ".*\\.woff",
      ".*\\.(t|o)tf",
      ".*\\.png",
      ".*\\.svg"
    ))

    val recorderConfig = props.build
    println("Using recorder config: " + recorderConfig)

    GatlingRecorder.fromMap(recorderConfig, Some(PathHelper.recorderConfUrl))
  }

  def startRecorderFromConfig(): Unit = {
    val configAsString = scala.io.Source.fromFile(PathHelper.gatlingImportConfig).getLines.mkString
    val configItems : List[Map[String,String]] = JSON.parseFull(configAsString).get.asInstanceOf[List[Map[String,String]]]

    for (configItem <- configItems) {
      def har = configItem.get("har").get
      def recorderWhiteList = configItem.get(GatlingProperties.RECORDER_WHITE_LIST).get
      def simulationClassName = configItem.get(GatlingProperties.SIMULATION_CLASS_NAME).get

      println("Got config:")
      println(" har='" + har + "'")
      println(" recorderWhiteList='" + recorderWhiteList + "'")
      println(" simulationClassName='" + simulationClassName + "'")

      var props = new RecorderPropertiesBuilder
      props.harFilePath(PathHelper.harInputDirectory + "/" + har)
      props.whitelist(util.Arrays.asList(recorderWhiteList))
      GatlingRecorderWrapper.startRecorder(props, simulationClassName)
    }

  }

  def copyGeneratedSimulation(): Unit = {
    // Copy generated simulation(s).
    //FileUtils.deleteDirectory(new File(PathHelper.simulationTargetFolder + "/simulations"))
    FileUtils.copyDirectory(new File(PathHelper.simulationOutputFolder), new File(PathHelper.simulationTargetFolder))

    // Copy generated request bodie(s) - when present.
    def bodiesFolder = new File(PathHelper.simulationRequestBodiesFolder)
    if (bodiesFolder.exists()) {
      FileUtils.copyDirectory(bodiesFolder, new File(PathHelper.requestBodiesTargetFolder))
    }

  }

  // Process config.
  GatlingRecorderWrapper.startRecorderFromConfig()

  // Copy generated simulation(s).
  GatlingRecorderWrapper.copyGeneratedSimulation()

}

