package controllers;

import models.Faq;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class FaqController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        Faq faq = Json.fromJson(json, Faq.class);
        faq.id = getObjectId(json);
        boolean create = faq.id == null;
        Faq.save(faq);
        if (create) {
            response().setHeader(LOCATION, "/v1/faqs/" + faq.id + ".json");
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        Faq.delete(id);
        return okWithHeaders();
    }

    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(Faq.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        Faq faq = Faq.find(id);
        if (faq != null) {
            return okJsonWithHeaders(faq);
        } else {
            return okWithHeaders();
        }
    }
}
