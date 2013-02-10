package controllers;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.libs.Json.*;
import play.mvc.Result;

public class BaseController extends Controller {

    public static void setHeaders() {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Access-Control-Allow-Headers", "origin, X-Requested-With, x-requested-with, content-type");
        response().setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
    }

    protected static Result okWithHeaders() {
        setHeaders();
        return ok();
    }

    protected static Result okJsonWithHeaders(Object obj) {
        setHeaders();
        return ok(Json.toJson(obj));
    }

    protected static ObjectId getObjectId(JsonNode json) {
        String uuid = json.findPath("uuid").getTextValue();
        return uuid == null ? null : new ObjectId(uuid);
    }

    protected static JsonNode getJsonFromBody() {
        return request().body().asJson();
    }
}
