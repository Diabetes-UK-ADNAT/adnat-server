package controllers;

import java.util.Date;
import java.util.List;
import play.mvc.Result;
import play.libs.Json.*;
import views.html.*;
import models.*;
import play.Logger;
import play.Play;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.auth.User;
import play.Routes;
import play.data.Form;
import play.mvc.*;
import play.mvc.Http.Response;
import play.mvc.Http.Session;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthProvider.MyLogin;
import providers.MyUsernamePasswordAuthProvider.MySignup;

import views.html.*;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import com.feth.play.module.pa.user.AuthUser;
import static controllers.BaseController.okWithHeaders;
import java.util.HashMap;
import org.codehaus.jackson.JsonNode;
import static play.mvc.Controller.response;
import static play.mvc.Http.HeaderNames.LOCATION;

import static play.mvc.Results.badRequest;
import scala.collection.Map;

public class Application extends BaseController {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";
    public static final String USER_ROLE = "user";

    public static Result options(String url) {
        Logger.debug(url);
        setHeaders();
        return ok();
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

    public static Result oAuthDenied(final String providerKey) {
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }

    public static Result index() {
        return ok(index.render());
    }

    public static User getLocalUser(final Session session) {
        final AuthUser currentAuthUser = PlayAuthenticate.getUser(session);
        final User localUser = User.findByAuthUserIdentity(currentAuthUser);
        return localUser;
    }

    @Restrict(
            @Group(Application.USER_ROLE))
    public static Result restricted() {
        final User localUser = getLocalUser(session());
        return ok(restricted.render(localUser));
    }

    @Restrict(
            @Group(Application.USER_ROLE))
    public static Result profile() {
        final User localUser = getLocalUser(session());
        return ok(profile.render(localUser));
    }

    public static Result login() {
        // FIXME for SPA return 401
        return ok(login.render(MyUsernamePasswordAuthProvider.LOGIN_FORM));
    }

    public static Result doLogin() {
        // if auth is ok then set auth token else leave empty for no auth
//        response().setHeader("X-AUTH-TOKEN", "h7sd-asf-JDUj2");

//        appKey for touch: 8C5F216E-6A3E-444B-8371-FC872A775112
        Logger.debug("doLogin");
        // no json it is a form JsonNode json = getJsonFromBody();
        //   Logger.debug(json.toString());
        final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
                .bindFromRequest();
        Logger.debug(filledForm.toString());
        
        
//        2013-04-14 00:59:17,224 44848434 [play-akka.actor.default-dispatcher-307] DEBUG - Form(of=class providers.MyUsernamePasswordAuthProvider$MyLogin, data={email=asdf@lsjkdf.com, _dc=1365919155463, password=sdfasa, appKey=8C5F216E-6A3E-444B-8371-FC872A775112}, value=Some(providers.MyUsernamePasswordAuthProvider$MyLogin@3f99e3ae), errors={})
        
        
        HashMap auth = new HashMap();
        auth.put("userToken", "h7sd-asf-JDUj2"); //FIXME REAL
        auth.put("userName", "Eric Link");//FIXME REAL
        return okJsonWithHeaders(auth);

//        return okWithHeaders();

//        // FIXME for SPA also do login action via json post return 401
//        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
//        final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
//                .bindFromRequest();
//        if (filledForm.hasErrors()) {
//            // User did not fill everything properly
//            Logger.debug(filledForm.toString());
//            Logger.debug("Login Error");
//            return badRequest(login.render(filledForm));
//        } else {
//            // Everything was filled
//             Logger.debug("Login via Provider");
//             return ok("You Win");
//            //return UsernamePasswordAuthProvider.handleLogin(ctx());
//        }
    }
//    public static Result doLogin() {
//        // FIXME for SPA also do login action via json post return 401
//        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
//        final Form<MyLogin> filledForm = MyUsernamePasswordAuthProvider.LOGIN_FORM
//                .bindFromRequest();
//        if (filledForm.hasErrors()) {
//            // User did not fill everything properly
//            Logger.debug("Login Error");
//            return badRequest(login.render(filledForm));
//        } else {
//            // Everything was filled
//             Logger.debug("Login via Provider");
//            return UsernamePasswordAuthProvider.handleLogin(ctx());
//        }
//    }

    public static Result signup() {
        return ok(signup.render(MyUsernamePasswordAuthProvider.SIGNUP_FORM));
    }

    public static Result jsRoutes() {
        return ok(
                Routes.javascriptRouter("jsRoutes",
                controllers.routes.javascript.Signup.forgotPassword()))
                .as("text/javascript");
    }

    public static Result doSignup() {
        com.feth.play.module.pa.controllers.Authenticate.noCache(response());
        final Form<MySignup> filledForm = MyUsernamePasswordAuthProvider.SIGNUP_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill everything properly
            return badRequest(signup.render(filledForm));
        } else {
            // Everything was filled
            // do something with your part of the form before handling the user
            // signup
            return UsernamePasswordAuthProvider.handleSignup(ctx());
        }
    }

    public static String formatTimestamp(final long t) {
        return new SimpleDateFormat("yyyy-dd-MM HH:mm:ss").format(new Date(t));
    }
}
