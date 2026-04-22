package com.example.performance.simulations;

import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;

import com.example.performance.config.TestConfig;
import com.example.performance.scenarios.UserJourneyScenario;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class LoadSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(TestConfig.BASE_URL)
            .acceptHeader("text/html")
            .userAgentHeader("Mozilla/5.0");

    {
        setUp(
                UserJourneyScenario.scenarioDef.injectOpen(
                        rampUsers(50).during(ofSeconds(30)),
                        constantUsersPerSec(10).during(ofMinutes(1))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile3().lt(Integer.valueOf(2000)),
                        global().successfulRequests().percent().gt(95.0)
                );
    }
}