package controllers;

import play.mvc.Result;
import play.mvc.BodyParser;
import play.libs.Json;
import play.libs.Json.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import views.html.*;
import models.*;
import play.Logger;

public class Application extends BaseController {

    public static Result options(String url) {
        Logger.debug(url);
        setHeaders();
        return ok();
    }

    public static Result index() { // FIXME remove
        return ok(index.render("Your new application is ready."));
    }

    public static Result group() { //FIXME remove
        Group g = new Group();
        g.groupName = "my group";
        g.date = new java.util.Date();
        Group.create(g);
        Logger.debug(g.groupName);
        Logger.debug(g.toString());

        Group.all();
        for (Group g2 : Group.all()) {
            Logger.debug(g2.toString());
        }
        Group.delete("50fef820978e61bbbd3cb95f");
        //Group.delete(g.id.toString());
        return ok("Your new application is ready.");
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result sayHello() { //FIXME remove
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        String name = json.findPath("name").getTextValue();
        if (name == null) {
            result.put("status", "KO");
            result.put("message", "Missing parameter [name]");
            return badRequest(result);
        } else {
            result.put("status", "OK");
            result.put("message", "Hello " + name);
            return ok(result);
        }
    }
}
