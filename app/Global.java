
import java.net.UnknownHostException;
import play.Logger;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import models.MorphiaObject;
import play.Play;
import play.GlobalSettings;
import play.mvc.Call;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;
import models.*;
import java.util.Arrays;

import models.auth.SecurityRole;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import controllers.routes;

import play.Application;
import play.GlobalSettings;
import play.mvc.Call;

import controllers.routes;
import providers.MyUsernamePasswordAuthProvider;

public class Global extends GlobalSettings {

    @Override
    public void onStart(play.Application arg0) {
        super.beforeStart(arg0);
        Logger.info("onStart");
        initMorphia();
        initPlayAuthenticate();
    }

    private void initMorphia() {
        try {
            MorphiaObject.mongo = new Mongo("127.0.0.1", 27017);
        } catch (UnknownHostException e) {
            Logger.error("Could not connect to database", e);
            Runtime.getRuntime().halt(-1);
        }

        MorphiaObject.morphia = new Morphia();
        //MorphiaObject.morphia.mapPackage("models", true);
		// fails even with ignore, so do all classes individually
        MorphiaObject.morphia.map(models.Ping.class);
        MorphiaObject.morphia.map(models.Person.class);
        MorphiaObject.morphia.map(models.Group.class);
        MorphiaObject.morphia.map(models.Assessment.class);
        MorphiaObject.morphia.map(models.Content.class);
        MorphiaObject.morphia.map(models.Faq.class);
        MorphiaObject.morphia.map(models.ContactRequest.class);
		//
        MorphiaObject.datastore = MorphiaObject.morphia.
                createDatastore(MorphiaObject.mongo,
                Play.application().configuration().getString("mongodb.name"));
        MorphiaObject.datastore.ensureIndexes();
        MorphiaObject.datastore.ensureCaps();

        Logger.info("Morphia datastore: " + MorphiaObject.datastore.getDB());
    }

    private void initPlayAuthenticate() {
        Logger.info("initPlayAuthenticate");
        PlayAuthenticate.setResolver(new Resolver() {
            @Override
            public Call login() {
                // Your login page
                return routes.Application.login();
            }

            @Override
            public Call afterAuth() {
                // The user will be redirected to this page after authentication
                // if no original URL was saved
                return routes.Application.index();
            }

            @Override
            public Call afterLogout() {
                return routes.Application.index();
            }

            @Override
            public Call auth(final String provider) {
                // You can provide your own authentication implementation,
                // however the default should be sufficient for most cases
                return com.feth.play.module.pa.controllers.routes.Authenticate
                        .authenticate(provider);
            }

            @Override
            public Call askMerge() {
                return routes.Account.askMerge();
            }

            @Override
            public Call askLink() {
                return routes.Account.askLink();
            }

            @Override
            public Call onException(final AuthException e) {
                if (e instanceof AccessDeniedException) {
                    return routes.Signup
                            .oAuthDenied(((AccessDeniedException) e)
                            .getProviderKey());
                }

                // more custom problem handling here...
                return super.onException(e);
            }
        });

        //FIXME DATA!!! initialData();
        Logger.info("PlayAuthenticate.resolver="+PlayAuthenticate.getResolver());
    }

    private void initialData() {
        if (SecurityRole.find.findRowCount() == 0) {
            for (final String roleName : Arrays
                    .asList(MyUsernamePasswordAuthProvider.USER_ROLE)) {
                final SecurityRole role = new SecurityRole();
                role.roleName = roleName;
                role.save();
            }
        }
    }
}