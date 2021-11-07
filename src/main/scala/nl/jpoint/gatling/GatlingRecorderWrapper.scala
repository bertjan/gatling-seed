package nl.jpoint.gatling

import io.gatling.recorder.GatlingRecorder
import io.gatling.recorder.config.{RecorderMode, RecorderPropertiesBuilder}
import org.apache.commons.io.FileUtils

import java.io.{File, FileInputStream}
import java.util
import java.util.Properties

object GatlingRecorderWrapper extends App {

  def startRecorder(props: RecorderPropertiesBuilder, simulationClassName : String): Unit = {

    println("Using project root dir " + PathHelper.projectRootDir)

    props.simulationsFolder(PathHelper.simulationOutputFolder)
    props.resourcesFolder(PathHelper.requestBodiesTargetFolder)
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
    val properties: Properties = new Properties()
    properties.load(new FileInputStream(PathHelper.gatlingImportConfig));

    def har = properties.getProperty("har")
    def recorderWhiteList = properties.getProperty(GatlingProperties.RECORDER_WHITE_LIST)
    def simulationClassName = properties.getProperty(GatlingProperties.SIMULATION_CLASS_NAME)

    println("Got config:")
    println(" har='" + har + "'")
    println(" recorderWhiteList='" + recorderWhiteList + "'")
    println(" simulationClassName='" + simulationClassName + "'")

    var props = new RecorderPropertiesBuilder
    props.harFilePath(PathHelper.harInputDirectory + "/" + har)
    props.whitelist(util.Arrays.asList(recorderWhiteList))
    GatlingRecorderWrapper.startRecorder(props, simulationClassName)

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

