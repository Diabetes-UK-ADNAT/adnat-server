package controllers;

import models.Group;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class GroupController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        Group group = Json.fromJson(json, Group.class);
        group.id = getObjectId(json);
        boolean create = group.id == null;
        Group.save(group);
        if (create) {
            response().setHeader(LOCATION, "/v1/groups/" + group.id);
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        Group.delete(id);
        return okWithHeaders();
    }

    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(Group.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        return okJsonWithHeaders(Group.find(id));
    }
}
