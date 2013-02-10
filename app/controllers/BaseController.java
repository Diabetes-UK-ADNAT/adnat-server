package controllers;

import play.mvc.Controller;
import play.libs.Json.*;

public class BaseController extends Controller {

    public static void setHeaders() {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Access-Control-Allow-Headers", "origin, X-Requested-With, x-requested-with, content-type");
        response().setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
    }
}
