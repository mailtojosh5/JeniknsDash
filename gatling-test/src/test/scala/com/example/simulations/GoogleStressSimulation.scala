import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GoogleStressSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.google.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")

  val stressScenario = scenario("Stress Test")
    .repeat(5) {
      exec(
        http("Search")
          .get("/search?q=performance")
          .check(status.in(200, 429))
      )
      .pause(500.milliseconds, 1000.milliseconds)
    }

  setUp(
    stressScenario.inject(
      atOnceUsers(30),
      rampUsers(20).during(10.seconds)
    )
  ).maxDuration(3.minutes)
   .protocols(httpProtocol)
   .assertions(
     global.responseTime.max.lt(15000),
     global.successfulRequests.percent.gt(85)
   )
}