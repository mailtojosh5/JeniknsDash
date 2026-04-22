package com.example.performance.simulations;

import static java.time.Duration.ofMinutes;

import com.example.performance.config.TestConfig;
import com.example.performance.scenarios.UserJourneyScenario;

import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class EnduranceSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(TestConfig.BASE_URL)
            .acceptHeader("application/json")
            .userAgentHeader("Mozilla/5.0");

    {
        setUp(
                UserJourneyScenario.scenarioDef.injectOpen(
                        constantUsersPerSec(5).during(ofMinutes(10))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().responseTime().percentile3().lt(Integer.valueOf(3000)),
                        global().responseTime().max().lt(Integer.valueOf(8000))
                );
    }
}