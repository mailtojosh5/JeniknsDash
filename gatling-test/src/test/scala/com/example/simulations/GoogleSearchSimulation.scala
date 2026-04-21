import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GoogleSearchSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.google.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")

  val searchScenario = scenario("Search Simulation")
    .exec(http("Homepage").get("/").check(status.is(200)))
    .pause(1, 2)
    .exec(http("Search Query").get("/search?q=gatling+performance+testing").check(status.is(200)))
    .pause(1)
    .exec(http("Page 2").get("/search?q=gatling+performance+testing&start=10").check(status.is(200)))

  setUp(
    searchScenario.inject(
      rampUsers(15).during(15.seconds),
      constantUsersPerSec(3).during(40.seconds)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.responseTime.percentile3.lt(5000),
     global.successfulRequests.percent.gt(90)
   )
}