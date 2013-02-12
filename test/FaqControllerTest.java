
import java.util.ArrayList;
import models.Faq;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import play.libs.Json;
import play.libs.WS;
import static play.mvc.Controller.request;

//import static org.fluentlenium.core.filter.FilterConstructor.*;
//import static play.mvc.Controller.response;
import static play.mvc.Http.Status.OK;

//GET	/v1/faqs.json		controllers.FaqController.getAll()
//GET	/v1/faqs/:id.json       controllers.FaqController.getById(id: String)
//POST	/v1/faqs.json		controllers.FaqController.save()
//#POST	/v1/faqs.json		controllers.FaqController.save2(faq: models.Faq)
//DELETE	/v1/faqs/:id.json	controllers.FaqController.delete(id: String)
public class FaqControllerTest {

    Faq faq = new Faq();

    private void createItem() {
        faq.question = "question";
        faq.answer = "answer";
        faq.categories = new ArrayList();
        faq.categories.add("one");
        faq.categories.add("two");
        JsonNode json = Json.toJson(faq);
        Result result =
                routeAndCall(fakeRequest(POST, "/v1/faqs.json")
                .withJsonBody(json));
        assertThat(status(result)).isEqualTo(OK);
        // location header
        // 201 returned
    }

    private void getItem() {
        // 200 
        // all fields
        // .equals orginal
    }

    private void updateItem() {
        // new values
        // get(faq) 
    }

    private void deleteItem() {
        // remove
        // get returns empty not found
    }

    @Test
    public void faqControllerItemTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                createItem();
                getItem();
                updateItem();
                deleteItem();
            }
        });
    }

    private void createItems() {
    }

    private void getItems() {
    }

    @Test
    public void faqControllerListTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                createItems();
                getItems();
//                deleteItems();
            }
        });
    }
}
