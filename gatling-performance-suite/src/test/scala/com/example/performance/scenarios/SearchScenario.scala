package com.example.performance.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

object SearchScenario {

  val search = scenario("Search Scenario")
    .exec(
      http("Load Homepage")
        .get("/")
        .check(status.is(200))
    )
    .pause(1, 2)
    .exec(
      http("Search Query")
        .get("/search?q=gatling+performance")
        .check(status.is(200))
    )
    .pause(1)
    .exec(
      http("Next Page")
        .get("/search?q=gatling+performance&start=10")
        .check(status.is(200))
    )
}