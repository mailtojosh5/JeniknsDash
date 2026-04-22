package com.example.performance.scenarios;

import io.gatling.javaapi.core.ChainBuilder;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class HomepageScenario {

    public static ChainBuilder homepage = exec(
            http("Open Homepage")
                    .get("/")
                    .check(status().is(200))
    );
}