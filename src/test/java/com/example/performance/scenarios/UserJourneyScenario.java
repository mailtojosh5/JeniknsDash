package com.example.performance.scenarios;

import io.gatling.javaapi.core.ScenarioBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;

public class UserJourneyScenario {

    public static ScenarioBuilder scenarioDef = scenario("User Journey")
            .exec(HomepageScenario.homepage)
            .pause(1)
            .exec(SearchScenario.search);
}