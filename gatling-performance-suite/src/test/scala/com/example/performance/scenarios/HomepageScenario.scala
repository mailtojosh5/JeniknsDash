package com.example.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object HomepageScenario {

  val homepage = scenario("Homepage Scenario")
    .exec(
      http("Open Homepage")
        .get("/")
        .check(status.is(200))
    )
}