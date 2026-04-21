package com.example.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.example.performance.scenarios.UserJourney
import com.example.performance.config.TestConfig

class StressSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(TestConfig.baseUrl)
    .acceptHeader("text/html")
    .userAgentHeader("Mozilla/5.0")

  setUp(
    UserJourney.scenarioDef.inject(
      rampUsers(200).during(2.minutes)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gt(85),
     global.responseTime.max.lt(10000)
   )
}