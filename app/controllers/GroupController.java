package controllers;

import be.objectify.deadbolt.java.actions.Restrict;
import models.Group;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;

public class GroupController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
	@Restrict(
			@be.objectify.deadbolt.java.actions.Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
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

	@Restrict(
			@be.objectify.deadbolt.java.actions.Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result delete(String id) {
		Logger.debug(id);
		Group.delete(id);
		return okWithHeaders();
	}

	@Restrict(
			@be.objectify.deadbolt.java.actions.Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result getAll() {
		Logger.debug("getAll");
		return okJsonWithHeaders(Group.all());
	}

	@Restrict(
			@be.objectify.deadbolt.java.actions.Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Group.find(id));
	}
}
