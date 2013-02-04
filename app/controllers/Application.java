package controllers;

import java.util.ArrayList;
import java.util.List;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import play.libs.Json;
import play.libs.Json.*;
import static play.libs.Json.toJson;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import views.html.*;
import models.*;
import play.Logger;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result group() {
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
    public static Result createFaq() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        String question = json.findPath("question").getTextValue();
        String answer = json.findPath("answer").getTextValue();
        if (question == null) {
            result.put("status", "KO");
            result.put("message", "Missing parameter [question]");
            return badRequest(result);
        } else {
            models.Faq faq = new models.Faq();
            faq.question = question;
            faq.answer = answer;
            Faq.create(faq);
            Logger.debug(faq.toString());
            return ok(result);
        }
    }

    private static void setOptions() {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Access-Control-Allow-Headers", "origin, X-Requested-With, x-requested-with, content-type");
        response().setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
    }

    public static Result options(String url) {
        Logger.debug(url);
        setOptions();
        return ok();
    }

    public static Result json() {
        setOptions();
        return ok(Json.toJson(Faq.all()));
    }

    public static Result jsonById(String id) {
        Logger.debug(id);
        setOptions();
        return ok(Json.toJson(Faq.find(id)));
    }


    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result sayHello() {
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
