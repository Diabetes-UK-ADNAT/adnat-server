package controllers;

import java.util.Date;
import java.util.List;
import play.mvc.Result;
import play.libs.Json.*;
import views.html.*;
import models.*;
import play.Logger;
import play.Play;

import static play.mvc.Results.badRequest;

public class Application extends BaseController {

    public static Result options(String url) {
        Logger.debug(url);
        setHeaders();
        return ok();
    }

    public static Result index() {
        play.Logger.info("index");
        return badRequest();
    }

    public static Result ping() {
        play.Logger.info("ping");

        //touch monitoring row
        Date currentTime = new Date();
        Ping pingModel = null;
        List<Ping> m = Ping.all();
        if (null == m || m.isEmpty()) {
            pingModel = new Ping();
        } else {
            pingModel = m.get(0);
        }
        pingModel.updated = currentTime;
        Ping.save(pingModel);

        String ack = "ACK";
        String deployDate = Play.application().configuration().getString("deploy.date");
        return ok(ping.render(ack, currentTime, deployDate));
    }
}
