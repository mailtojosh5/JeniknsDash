package com.example.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.Duration;

public class ReqResApiSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("https://reqres.in")
      .acceptHeader("application/json")
      .contentTypeHeader("application/json");

    ScenarioBuilder scn = scenario("ReqRes API Test")
      .exec(
        http("Get Users")
          .get("/api/users?page=2")
          .check(status().is(200))
          .check(jsonPath("$.page").is("2"))
      )
      .pause(Duration.ofSeconds(1))
      .exec(
        http("Get Single User")
          .get("/api/users/2")
          .check(status().is(200))
          .check(jsonPath("$.data.id").is("2"))
      )
      .pause(Duration.ofSeconds(1))
      .exec(
        http("Create User")
          .post("/api/users")
          .body(StringBody("{\"name\": \"john\", \"job\": \"qa engineer\"}")).asJson()
          .check(status().is(201))
          .check(jsonPath("$.name").is("john"))
      );

    setUp(
      scn.injectOpen(
        rampUsers(20).during(Duration.ofSeconds(10)),
        constantUsersPerSec(5).during(Duration.ofSeconds(20))
      )
    ).protocols(httpProtocol)
      .assertions(
        global().responseTime().max().lt(5000),
        global().successfulRequests().percent().gt(95.0)
      );
  }
}
