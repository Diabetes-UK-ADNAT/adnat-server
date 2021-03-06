package controllers;

import com.feth.play.module.pa.PlayAuthenticate;
import models.Person;
import models.auth.User;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.libs.Json.*;
import static play.mvc.Controller.response;
import play.mvc.Result;

public class BaseController extends Controller {

	public static void setHeaders() {
		response().setHeader("Access-Control-Allow-Origin",
				"*");
		response().setHeader("Access-Control-Allow-Headers",
				"origin, Content-Type, X-Requested-With, x-requested-with, content-type, X-Auth-Token, X-App-Key");
		response().setHeader("Access-Control-Allow-Methods",
				"PUT, GET, POST, DELETE, OPTIONS");
		// http://www.w3.org/TR/2008/WD-access-control-20080912/
		
		response().setHeader("Access-Control-Max-Age", "10");
		//IE caches resource calls agressively, so make sure no-cache is explicit
		response().setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response().setHeader("Pragma", "no-cache");
		response().setHeader("Expires", "0");

		// response().setHeader("Access-Control-Max-Age", "1728000");
		// http://stackoverflow.com/questions/7067966/how-to-allow-cors-in-express-nodejs
	}

	protected static Result okWithHeaders() {
		setHeaders();
		return ok();
	}

	protected static Result okJsonWithHeaders(Object obj) {
		setHeaders();
		if (obj != null) {
			return ok(Json.toJson(obj));
		} else {
			return okWithHeaders();
		}
	}

	protected static ObjectId getObjectId(JsonNode json) {
		JsonNode uuid = json.get("uuid");
		return uuid == null ? null : new ObjectId(uuid.getTextValue());
	}

	protected static JsonNode getJsonFromBody() {
		return request().body().asJson();
	}

	protected static Person getSubject() {
		User u = User.findByAuthUserIdentity(PlayAuthenticate.getUser(session()));
		Logger.debug("User=" + u);
		Person subject = u == null ? null : Person.findByAccount(u.uuid);
		Logger.debug("Subj=" + subject);
		if (subject != null) {
			StringBuilder sb = new StringBuilder();
			for (String r : subject.roles) {
				sb.append(r);
				sb.append(',');
			}
			Logger.debug("subject.uuid=" + subject.getUUID());
			Logger.debug("subject.roles=" + sb.toString());
			Logger.debug("subject.site=" + subject.site);
		}
		return subject;
	}
}
