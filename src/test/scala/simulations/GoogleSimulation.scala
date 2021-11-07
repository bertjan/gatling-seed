package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GoogleSimulation extends Simulation {

  val httpProtocol = http.baseUrl("https://www.google.nl").userAgentHeader("test")

  val myFirstScenario = scenario("Google search")
    .exec(
      http("Open the Google start page.")
        .get("/")
        .check(status.is(200))
    )
    .pause(1)
    .exec(
      http("Perform Google search on 'gatling'.")
        .get("/search?q=gatling")
        .check(status.is(200))
//        .check( bodyString.saveAs( "RESPONSE_DATA" ) ))
//        .exec( session => { println("Response: " + session("RESPONSE_DATA")); session }
        .check(regex("<title>Voordat je verdergaat naar Google Zoeken</title>"))
    )

  setUp(myFirstScenario.inject(rampUsers(1) during (5 seconds))).protocols(httpProtocol)

}
