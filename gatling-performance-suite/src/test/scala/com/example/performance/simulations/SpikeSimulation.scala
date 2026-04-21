package com.example.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.example.performance.scenarios.UserJourney
import com.example.performance.config.TestConfig

class SpikeSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(TestConfig.baseUrl)
    .acceptHeader("text/html")
    .userAgentHeader("Mozilla/5.0")

  setUp(
    UserJourney.scenarioDef.inject(
      nothingFor(5.seconds),
      atOnceUsers(100)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gt(90),
     global.responseTime.percentile3.lt(4000)
   )
}