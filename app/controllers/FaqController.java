package controllers;

import models.Faq;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class FaqController extends BaseController {

    public static Result save2(Faq faq) {
        Logger.debug(faq.toString());
        return ok();
    }

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        String uuid = json.findPath("uuid").getTextValue();
        String category = json.findPath("category").getTextValue();
        String question = json.findPath("question").getTextValue();
        String answer = json.findPath("answer").getTextValue();
        setHeaders();
        if (question == null) {
            result.put("status", "KO");
            result.put("message", "Missing parameter [question]");
            return badRequest(result);
        } else {
            Faq faq = new Faq();
            faq.id = uuid == null ? null : new ObjectId(uuid);
            faq.category = category;
            faq.question = question;
            faq.answer = answer;
            Faq.save(faq);
            Logger.debug(faq.toString());
            return ok(result);
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        setHeaders();
        Faq.delete(id);
        return ok();
    }

    public static Result getAll() {
        setHeaders();
        return ok(Json.toJson(Faq.all()));
    }

    public static Result getById(String id) {
        Logger.debug(id);
        setHeaders();
        return ok(Json.toJson(Faq.find(id)));
    }
}
