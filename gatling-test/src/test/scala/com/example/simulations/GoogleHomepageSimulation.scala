import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GoogleHomepageSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.google.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")

  val homepageScenario = scenario("Homepage Access")
    .exec(
      http("GET Homepage")
        .get("/")
        .check(status.is(200))
    )

  setUp(
    homepageScenario.inject(
      rampUsers(10).during(10.seconds),
      constantUsersPerSec(2).during(30.seconds)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.responseTime.max.lt(10000),
     global.successfulRequests.percent.gt(95)
   )
}