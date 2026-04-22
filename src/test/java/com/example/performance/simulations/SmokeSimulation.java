package com.example.performance.simulations;

import com.example.performance.config.TestConfig;
import com.example.performance.scenarios.HomepageScenario;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import io.gatling.javaapi.core.Simulation;
import static io.gatling.javaapi.http.HttpDsl.http;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class SmokeSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(TestConfig.BASE_URL)
            .acceptHeader("text/html")
            .userAgentHeader("Mozilla/5.0");

    {
        setUp(
                scenario("Smoke Test").exec(HomepageScenario.homepage).injectOpen(
                        atOnceUsers(5)
                )
        ).protocols(httpProtocol)
                .assertions(
                        global().successfulRequests().percent().gt(95.0),
                        global().responseTime().max().lt(Integer.valueOf(3000))
                );
    }
}