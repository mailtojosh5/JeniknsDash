package com.example.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.example.performance.scenarios.UserJourney
import com.example.performance.config.TestConfig

class EnduranceSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(TestConfig.baseUrl)
    .acceptHeader("application/json")
    .userAgentHeader("Mozilla/5.0")

  setUp(
    UserJourney.scenarioDef.inject(
      constantUsersPerSec(5).during(10.minutes)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gt(90),
     global.responseTime.percentile3.lt(3000),
     global.responseTime.max.lt(8000)
   )
}