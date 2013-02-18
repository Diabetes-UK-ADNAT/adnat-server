package controllers;

import models.Content;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class ContentController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        Content content = Json.fromJson(json, Content.class);
        content.id = getObjectId(json);
        boolean create = content.id == null;
        Content.save(content);
        if (create) {
            response().setHeader(LOCATION, "/v1/contents/" + content.id + ".json");
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        Content.delete(id);
        return okWithHeaders();
    }

    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(Content.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        return okJsonWithHeaders(Content.find(id));
    }
}
