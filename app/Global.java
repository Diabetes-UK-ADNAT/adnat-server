
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


import controllers.routes;

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
        MorphiaObject.datastore = MorphiaObject.morphia.
                createDatastore(MorphiaObject.mongo,
                Play.application().configuration().getString("mongodb.name"));
        MorphiaObject.datastore.ensureIndexes();
        MorphiaObject.datastore.ensureCaps();

        Logger.info("Morphia datastore: " + MorphiaObject.datastore.getDB());
    }

    private void initPlayAuthenticate() {
        PlayAuthenticate.setResolver(new Resolver() {
            @Override
            public Call login() {
                // Your login page
                return routes.Application.index();
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
            public Call onException(final AuthException e) {
                if (e instanceof AccessDeniedException) {
                    return routes.Application
                            .oAuthDenied(((AccessDeniedException) e)
                            .getProviderKey());
                }
                // more custom problem handling here...
                return super.onException(e);
            }

            @Override
            public Call askLink() {
                // We don't support moderated account linking in this sample.
                // See the play-authenticate-usage project for an example
                return null;
            }

            @Override
            public Call askMerge() {
                // We don't support moderated account merging in this sample.
                // See the play-authenticate-usage project for an example
                return null;
            }
        });
    }
}