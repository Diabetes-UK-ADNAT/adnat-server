package controllers;

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
        return ok("Your new application is ready.");
	}
    public static Result json() {
		ObjectNode result = Json.newObject();
		result.put("status", "OK");
		result.put("message", "Hello ");
		return ok(result);
    }

	@BodyParser.Of(play.mvc.BodyParser.Json.class)
	public static Result sayHello() {
		JsonNode json = request().body().asJson();
		ObjectNode result = Json.newObject();
		String name = json.findPath("name").getTextValue();
		if(name == null) {
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
