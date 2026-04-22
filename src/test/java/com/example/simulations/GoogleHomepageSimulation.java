package com.example.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.Duration;

public class GoogleHomepageSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("https://www.google.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

    ScenarioBuilder homepageScenario = scenario("Homepage Access")
      .exec(
        http("GET Homepage")
          .get("/")
          .check(status().is(200))
      );

    setUp(
      homepageScenario.injectOpen(
        rampUsers(10).during(Duration.ofSeconds(10)),
        constantUsersPerSec(2).during(Duration.ofSeconds(30))
      )
    ).protocols(httpProtocol)
      .assertions(
        global().responseTime().max().lt(10000),
        global().successfulRequests().percent().gt(95.0)
      );
  }
}
