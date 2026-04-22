package com.example.performance.simulations;

import static java.time.Duration.ofSeconds;

import com.example.performance.config.TestConfig;
import com.example.performance.scenarios.UserJourneyScenario;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class SpikeSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(TestConfig.BASE_URL)
            .acceptHeader("text/html")
            .userAgentHeader("Mozilla/5.0");

    {
        setUp(
                UserJourneyScenario.scenarioDef.injectOpen(
                        nothingFor(ofSeconds(5)),
                        atOnceUsers(100)
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(90.0),
                        global().responseTime().percentile3().lt(Integer.valueOf(4000))
                );
    }
}