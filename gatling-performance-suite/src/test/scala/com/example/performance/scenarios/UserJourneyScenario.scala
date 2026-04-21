package com.example.performance.scenarios

import io.gatling.core.Predef._
import scala.concurrent.duration._

object UserJourney {

  import HomepageScenario._
  import SearchScenario._

  val scenarioDef = scenario("User Journey")
    .exec(homepage)
    .pause(1)
    .exec(search)
}