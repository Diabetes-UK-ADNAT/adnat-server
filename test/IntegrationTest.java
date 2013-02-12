
import org.junit.*;

import play.mvc.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

//import static org.fluentlenium.core.filter.FilterConstructor.*;
//import static play.mvc.Controller.response;
import static play.mvc.Http.Status.OK;

public class IntegrationTest {

    @Test
    public void optionsControllerTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result;
                result = callAction(controllers.routes.ref.Application.options("URL"));
                // fixme test headers
                assertThat(header("Access-Control-Allow-Origin", result))
                        .isEqualTo("*");
                assertThat(header("Access-Control-Allow-Headers", result))
                        .isEqualTo("origin, X-Requested-With, x-requested-with, content-type");
                assertThat(header("Access-Control-Allow-Methods", result))
                        .isEqualTo("PUT, GET, POST, DELETE, OPTIONS");
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo(null);
                assertThat(charset(result)).isEqualTo(null);
                assertThat(contentAsString(result)).contains("");
            }
        });
    }

    @Test
    public void badRoute() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = routeAndCall(fakeRequest(GET, "/nadaroute"));
                assertThat(result).isNull();
            }
        });
    }

    @Test
    public void indexControllerTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                //Result result = routeAndCall(fakeRequest(GET, "/index"));
                Result result = callAction(controllers.routes.ref.Application.index());
                assertThat(status(result)).isEqualTo(400);
                assertThat(contentType(result)).isEqualTo(null);
                assertThat(charset(result)).isEqualTo(null);
                assertThat(contentAsString(result)).contains("");
            }
        });
    }

    @Test
    public void pingControllerTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                //Result result = callAction(controllers.routes.ref.Application.ping());
                Result result = routeAndCall(fakeRequest(GET, "/v1/ping"));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("text/html");
                assertThat(charset(result)).isEqualTo("utf-8");
                assertThat(contentAsString(result)).contains("ACK");
            }
        });
    }
}
