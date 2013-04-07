package controllers;

import models.ContactRequest;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class ContactRequestController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        ContactRequest contactRequest = Json.fromJson(json, ContactRequest.class);
        contactRequest.id = getObjectId(json);
        boolean create = contactRequest.id == null;
        ContactRequest.save(contactRequest);
        if (create) {
            response().setHeader(LOCATION, "/v1/contactrequests/" + contactRequest.id);
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        ContactRequest.delete(id);
        return okWithHeaders();
    }

    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(ContactRequest.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        return okJsonWithHeaders(ContactRequest.find(id));
    }
}
