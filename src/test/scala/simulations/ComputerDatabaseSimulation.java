package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * This sample is based on our official tutorials:
 * <ul>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/quickstart">Gatling quickstart tutorial</a>
 *   <li><a href="https://gatling.io/docs/gatling/tutorials/advanced">Gatling advanced tutorial</a>
 * </ul>
 */
public class ComputerDatabaseSimulation extends Simulation {

  ChainBuilder v1 = exec(http("V1").get("/v1/reviews/search?q=Donald"));
  ChainBuilder v2 = exec(http("V2").get("/v2/reviews/search?q=Donald"));

  HttpProtocolBuilder httpProtocol =
    http.baseUrl("http://localhost:8080")
      .acceptHeader("application/json;q=0.9,*/*;q=0.8")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader(
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
      );

  ScenarioBuilder users = scenario("Users").exec(v1, v2);

  {
    setUp(
      users.injectOpen(rampUsers(10).during(10))
    ).protocols(httpProtocol);
  }
}