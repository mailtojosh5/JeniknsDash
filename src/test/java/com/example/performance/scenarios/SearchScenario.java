package com.example.performance.scenarios;

import io.gatling.javaapi.core.ChainBuilder;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class SearchScenario {

    public static ChainBuilder search = exec(
            http("Load Homepage")
                    .get("/")
                    .check(status().is(200))
    )
    .pause(1, 2)
    .exec(
            http("Search Query")
                    .get("/search?q=gatling+performance")
                    .check(status().is(200))
    )
    .pause(1)
    .exec(
            http("Next Page")
                    .get("/search?q=gatling+performance&start=10")
                    .check(status().is(200))
    );
}