package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.BodyParser;
import play.libs.Json;
import play.libs.Json.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import views.html.*;
import models.*;
import play.Logger;
import org.bson.types.ObjectId;

public class BaseController extends Controller {

    public static void setHeaders() {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Access-Control-Allow-Headers", "origin, X-Requested-With, x-requested-with, content-type");
        response().setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
    }

}
