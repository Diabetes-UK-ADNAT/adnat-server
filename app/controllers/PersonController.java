package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Person;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class PersonController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        JsonNode json = getJsonFromBody();
        Person person = Json.fromJson(json, Person.class);
        person.id = getObjectId(json);
        boolean create = person.id == null;
        Person.save(person);
        if (create) {
            response().setHeader(LOCATION, "/v1/persons/" + person.id);
            setHeaders();
            return created();
        } else {
            return okWithHeaders();
        }
    }

    public static Result delete(String id) {
        Logger.debug(id);
        Person.delete(id);
        return okWithHeaders();
    }

//    @Restrict(
//            @Group(Application.USER_ROLE))
    public static Result getAll() {
        Logger.debug("getAll");
        return okJsonWithHeaders(Person.all());
    }

    public static Result getById(String id) {
        Logger.debug(id);
        return okJsonWithHeaders(Person.find(id));
    }
}
