package com.example.performance.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import com.example.performance.scenarios.UserJourney
import com.example.performance.config.TestConfig

class LoadSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl(TestConfig.baseUrl)
    .acceptHeader("text/html")
    .userAgentHeader("Mozilla/5.0")

  setUp(
    UserJourney.scenarioDef.inject(
      rampUsers(50).during(30.seconds),
      constantUsersPerSec(10).during(1.minute)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.responseTime.percentile3.lt(2000),
     global.successfulRequests.percent.gt(95)
   )
}