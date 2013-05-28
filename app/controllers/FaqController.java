package controllers;

import be.objectify.deadbolt.java.actions.Dynamic;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Faq;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;

public class FaqController extends BaseController {

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	@BodyParser.Of(value = BodyParser.Json.class)
	public static Result save() {
		Logger.debug("save");
		JsonNode json = getJsonFromBody();
		Faq faq = Json.fromJson(json, Faq.class);
		faq.id = getObjectId(json);
		boolean create = faq.id == null;
		Faq.save(faq);
		if (create) {
			response().setHeader(LOCATION, "/v1/faqs/" + faq.id);
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
		Faq.delete(id);
		return okWithHeaders();
	}

	@Dynamic(value = "appKey")
	public static Result getAll() {
		Logger.debug("getAll");
		return okJsonWithHeaders(Faq.all());
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Faq.find(id));
	}
}
