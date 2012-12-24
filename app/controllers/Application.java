package controllers;

import play.*;
import play.mvc.*;
import play.libs.*;
import org.codehaus.jackson.node.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
	
    public static Result json() {
		ObjectNode result = Json.newObject();
		result.put("status", "OK");
		result.put("message", "Hello ");
		return ok(result);
    }
  
}
