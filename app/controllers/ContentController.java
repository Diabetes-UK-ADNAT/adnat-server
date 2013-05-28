package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Content;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;

public class ContentController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        Content content = Json.fromJson(json, Content.class);
        content.id = getObjectId(json);
        boolean create = content.id == null;
        Content.save(content);
        if (create) {
            response().setHeader(LOCATION, "/v1/contents/" + content.id);
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
    public static Result delete(String id) {
        Logger.debug(id);
        Content.delete(id);
        return okWithHeaders();
    }

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(Content.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        return okJsonWithHeaders(Content.find(id));
    }
}
