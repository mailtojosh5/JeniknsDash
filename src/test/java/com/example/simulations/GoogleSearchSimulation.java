package com.example.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import java.time.Duration;

public class GoogleSearchSimulation extends Simulation {

  {
    HttpProtocolBuilder httpProtocol = http
      .baseUrl("https://www.google.com")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

    ScenarioBuilder searchScenario = scenario("Search Simulation")
      .exec(http("Homepage").get("/").check(status().is(200)))
      .pause(Duration.ofSeconds(1), Duration.ofSeconds(2))
      .exec(http("Search Query").get("/search?q=gatling+performance+testing").check(status().is(200)))
      .pause(Duration.ofSeconds(1))
      .exec(http("Page 2").get("/search?q=gatling+performance+testing&start=10").check(status().is(200)));

    setUp(
      searchScenario.injectOpen(
        rampUsers(15).during(Duration.ofSeconds(15)),
        constantUsersPerSec(3).during(Duration.ofSeconds(40))
      )
    ).protocols(httpProtocol)
      .assertions(
        global().responseTime().percentile(3).lt(5000),
        global().successfulRequests().percent().gt(90.0)
      );
  }
}
