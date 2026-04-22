package com.example.performance.simulations;

import static java.time.Duration.ofMinutes;

import com.example.performance.config.TestConfig;
import com.example.performance.scenarios.UserJourneyScenario;

import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class StressSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(TestConfig.BASE_URL)
            .acceptHeader("text/html")
            .userAgentHeader("Mozilla/5.0");

    {
        setUp(
                UserJourneyScenario.scenarioDef.injectOpen(
                        rampUsers(200).during(ofMinutes(2))
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(85.0),
                        global().responseTime().max().lt(Integer.valueOf(10000))
                );
    }
}