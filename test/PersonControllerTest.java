
import java.util.ArrayList;
import models.Name;
import models.Person;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.*;
import play.mvc.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import play.libs.Json;

//import static org.fluentlenium.core.filter.FilterConstructor.*;
//import static play.mvc.Controller.response;
import static play.mvc.Http.Status.OK;

public class PersonControllerTest {

    String location = null;

    private void createItem() {
        Person person = new Person();
        person.name = new Name();
        person.name.firstNames = "First Names";
        person.name.lastName = "Last Name";
        JsonNode json = Json.toJson(person);
        System.out.println(json);
        Result result = routeAndCall(fakeRequest(POST, "/v1/persons.json").withJsonBody(json));
        assertThat(result).isNotNull();
        assertThat(status(result)).isEqualTo(201);
        location = header("Location", result);
        assertThat(location).matches("^/v1/persons/.*\\.json");
    }

    private JsonNode getItem() {
        try {
            Result result = routeAndCall(fakeRequest(GET, location));
            assertThat(status(result)).isEqualTo(OK);
            ObjectMapper mapper = new ObjectMapper();
            assertThat(contentAsString(result)).isNotNull();
            return mapper.readTree(contentAsString(result));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateItem(JsonNode json) { // fixme json serializing confusion
        // update the item; change pojo and POST to location
//        Result result =
//                routeAndCall(fakeRequest(POST, location)
//                .withJsonBody(json));
        //assertThat(status(result)).isEqualTo(OK);
        //assertThat(header("Location", result)).isEmpty();
    }

    private void deleteItem() {
        Result result = routeAndCall(fakeRequest(DELETE, location));
        assertThat(status(result)).isEqualTo(OK);
    }

    @Test
    public void itemCrud() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                createItem();
                updateItem(getItem());
                deleteItem();
            }
        });
    }

    @Test
    public void list() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result;
                createItem();
                String loc1 = location;
                createItem();
                String loc2 = location;
                createItem();
                String loc3 = location;
                //
                result = routeAndCall(fakeRequest(GET, "/v1/persons.json"));
                assertThat(status(result)).isEqualTo(OK);
                String list = contentAsString(result);
                assertThat(list).isNotNull();
                assertThat(list).isNotEmpty();
                assertThat(list).matches("^\\[\\{.*id.*name.*first.*id.*name.*first.*");
                //
                result = routeAndCall(fakeRequest(DELETE, loc1));
                assertThat(status(result)).isEqualTo(OK);
                result = routeAndCall(fakeRequest(DELETE, loc2));
                assertThat(status(result)).isEqualTo(OK);
                result = routeAndCall(fakeRequest(DELETE, loc3));
                assertThat(status(result)).isEqualTo(OK);
            }
        });
    }

    @Test
    public void itemNotFound() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                createItem();
                //
                Result result = routeAndCall(fakeRequest(GET, "/v1/persons/5119e5293004dab11d02cc49.json"));
                assertThat(status(result)).isEqualTo(OK);
                String list = contentAsString(result);
                assertThat(list).isEmpty();
                //
                deleteItem();
            }
        });
    }
}
