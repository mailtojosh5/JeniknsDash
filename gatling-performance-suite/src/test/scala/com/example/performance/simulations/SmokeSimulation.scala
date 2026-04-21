package com.example.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import com.example.performance.scenarios.HomepageScenario
import com.example.performance.config.TestConfig

class SmokeSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(TestConfig.baseUrl)
    .acceptHeader("text/html")
    .userAgentHeader("Mozilla/5.0")

  setUp(
    HomepageScenario.homepage.inject(atOnceUsers(5))
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gt(95),
     global.responseTime.max.lt(3000)
   )
}