package controllers;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.libs.Json.*;
import play.mvc.Http;
import play.mvc.Result;

public class BaseController extends Controller {

	public static void setHeaders() {
		response().setHeader("Access-Control-Allow-Origin",
				"*");
		response().setHeader("Access-Control-Allow-Headers",
				"origin, Content-Type, X-Requested-With, x-requested-with, content-type, X-Auth-Token, X-App-Key");
		response().setHeader("Access-Control-Allow-Methods",
				"PUT, GET, POST, DELETE, OPTIONS");
		response().setHeader("Access-Control-Max-Age", "1728000");
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

}
