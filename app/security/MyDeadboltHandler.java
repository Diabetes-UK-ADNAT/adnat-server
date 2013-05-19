package security;

import models.auth.User;
import play.mvc.Http;
import play.mvc.Result;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDynamicResourceHandler;
import be.objectify.deadbolt.java.DeadboltHandler;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUserIdentity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import play.Logger;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

	@Override
	public Result beforeAuthCheck(final Http.Context context) {
		setSessionFromAuthToken(context);

		// uris w/o app keys
		if (context.request().uri().equals("/profile")
				|| context.request().uri().equals("/accounts/password/change")) {
			return null;
		};

		// app keys for all other uris
		String xAppKey = context.request().getHeader("X-App-Key");
		Logger.debug("appKey=" + xAppKey);
		if (xAppKey != null
				&& xAppKey.equals("13B6EFE5-63EE-4F1C-A486-76B24AAE1704")) {
			// user is logged in
			return null;
		} else {
			// user is not logged in
			// call this if you want to redirect your visitor to the page that
			// was requested before sending him to the login page
			// if you don't call this, the user will get redirected to the page
			// defined by your resolver
			final String originalUrl = PlayAuthenticate.storeOriginalUrl(context);
			context.flash().put("error", "You need to log in first, to view '" + originalUrl + "'");
			return redirect(PlayAuthenticate.getResolver().login());
		}
	}

	@Override
	public Subject getSubject(final Http.Context context) {
		final AuthUserIdentity u = PlayAuthenticate.getUser(context);
		// Caching might be a good idea here
		return User.findByAuthUserIdentity(u);
	}

	@Override
	public DynamicResourceHandler getDynamicResourceHandler(
			final Http.Context context) {
		return new AbstractDynamicResourceHandler() {
			@Override
			public boolean isAllowed(String name,
					String meta,
					DeadboltHandler deadboltHandler,
					Http.Context context) {

				String xAppKey = context.request().getHeader("X-App-Key");
				Logger.debug("dynamic=" + xAppKey);
				return xAppKey != null && xAppKey.equals("13B6EFE5-63EE-4F1C-A486-76B24AAE1704");
			}
		};
	}

	@Override
	public Result onAuthFailure(final Http.Context context,
			final String content) {
		// if the user has a cookie with a valid user and the local user has
		// been deactivated/deleted in between, it is possible that this gets
		// shown. You might want to consider to sign the user out in this case.
		return forbidden("Forbidden");
	}

	private void setSessionFromAuthToken(final Http.Context context) {
		try {
			String xAuthToken = context.request().getHeader("X-Auth-Token");
			Logger.debug("beforeAuthCheck:" + xAuthToken);
			if (xAuthToken != null) {
				String decoded = URLDecoder.decode(xAuthToken, "UTF-8");
				Logger.debug("beforeAuthCheck:" + decoded);
				//HashMap<String, String> newSession = new HashMap<String, String>();
				String[] split = xAuthToken.split("-");
				if (split.length > 1) {
					String[] split2 = split[1].split("%00");
					context.session().clear();
					for (int i = 0; i < split2.length; i++) {
						String[] keyVal = split2[i].split(":");
						context.session().put(keyVal[0], keyVal[1]);
					}
				}
			}
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(MyDeadboltHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		Logger.debug(context.session().toString());
	}
}
