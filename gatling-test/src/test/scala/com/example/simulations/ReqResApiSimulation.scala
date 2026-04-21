

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ReqResApiSimulation extends Simulation {

  // 🌐 HTTP configuration
  val httpProtocol = http
    .baseUrl("https://reqres.in")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // 📡 Scenario definition
  val scn = scenario("ReqRes API Test")

    // 🔹 GET request - list users
    .exec(
      http("Get Users")
        .get("/api/users?page=2")
        .check(status.is(200))
        .check(jsonPath("$.page").is("2"))
    )

    .pause(1)

    // 🔹 GET single user
    .exec(
      http("Get Single User")
        .get("/api/users/2")
        .check(status.is(200))
        .check(jsonPath("$.data.id").is("2"))
    )

    .pause(1)

    // 🔹 POST request - create user
    .exec(
      http("Create User")
        .post("/api/users")
        .body(StringBody(
          """
          {
            "name": "john",
            "job": "qa engineer"
          }
          """
        )).asJson
        .check(status.is(201))
        .check(jsonPath("$.name").is("john"))
    )

  // 🚀 Load simulation
  setUp(
    scn.inject(
      rampUsers(20).during(10.seconds),
      constantUsersPerSec(5).during(20.seconds)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.max.lt(5000),
      global.successfulRequests.percent.gt(95)
    )
}