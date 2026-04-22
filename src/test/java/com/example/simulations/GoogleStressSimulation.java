package com.example.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.Duration;

public class GoogleStressSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("https://www.google.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

    ScenarioBuilder stressScenario = scenario("Stress Test")
      .repeat(5).on(
        exec(
          http("Search")
            .get("/search?q=performance")
            .check(status().in(200, 429))
        )
        .pause(Duration.ofMillis(500), Duration.ofMillis(1000))
      );

    setUp(
      stressScenario.injectOpen(
        atOnceUsers(30),
        rampUsers(20).during(Duration.ofSeconds(10))
      )
    ).maxDuration(Duration.ofMinutes(3))
      .protocols(httpProtocol)
      .assertions(
        global().responseTime().max().lt(15000),
        global().successfulRequests().percent().gt(85.0)
      );
  }
}
