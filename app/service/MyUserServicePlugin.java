package service;

import models.Person;
import play.Application;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;

public class MyUserServicePlugin extends UserServicePlugin {

    public MyUserServicePlugin(final Application app) {
        super(app);
    }

    @Override
    public Object save(final AuthUser authUser) {
        final boolean isLinked = false;//FIXME Person.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return new Person();//FIXME Person.create(authUser).id;
        } else {
            // we have this user already, so return null
            return null;
        }
    }

    @Override
    public Object getLocalIdentity(final AuthUserIdentity identity) {
        // For production: Caching might be a good idea here...
        // ...and dont forget to sync the cache when users get deactivated/deleted
        final Person u = null;//FIXME Person.findByAuthUserIdentity(identity);
        if (u != null) {
            return u.id;
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            return oldUser;//FIXME Person.merge(oldUser, newUser);
        }
        return oldUser;

    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        //FIXME Person.addLinkedAccount(oldUser, newUser);
        return null;
    }
}
