package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import java.util.ArrayList;
import java.util.Iterator;
import models.Person;
import models.auth.SecurityRole;
import models.auth.User;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;

public class PersonController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
	public static Result save() {
		Logger.debug("save");
		JsonNode json = getJsonFromBody();
		Logger.debug(json.toString());
		// get person 
		Person person = Json.fromJson(json, Person.class);
		person.id = getObjectId(json);
		boolean create = person.id == null;

		// handle referenced objects
		if (person.site == null) { //fixme bogus site example
			// error required field
		} else {
			person.site.id = new ObjectId(json.findPath("site").findPath("uuid").getTextValue());
			models.Group.save(person.site);
		}

		person.careTeam = new ArrayList();
		Iterator<JsonNode> teamNodes = json.findPath("careTeam").getElements();
		while (teamNodes.hasNext()) {
			JsonNode n = teamNodes.next();
			Person p = Json.fromJson(n, Person.class);
			p.id = new ObjectId(n.get("uuid").getTextValue());
			person.careTeam.add(p);
		}

// pwd != null && == then set user
// if not found create, with password
//// email update requires new password; or save original email so can update as it's the key

		//PlayAuthenticate.storeUser(session(), authUser);
		//User u = User.findByUsernamePasswordIdentity(user);
		User u = User.findByEmail(person.email);
		if (u != null) {
			u.deleteManyToManyAssociations("roles");
			for (String role : person.roles) {
				u.roles.add(SecurityRole.findByRoleName(role.toLowerCase()));
			}
			u.saveManyToManyAssociations("roles");
		} else {
			MyUsernamePasswordAuthProvider.MySignup signup = new MyUsernamePasswordAuthProvider.MySignup();
			signup.email = person.email;
			signup.password = person.password;
			signup.repeatPassword = person.password;
			person.password = null;
			signup.name = signup.email;
			MyUsernamePasswordAuthUser user = new MyUsernamePasswordAuthUser(signup);
			u = User.create(user);
			u.roles = new ArrayList<SecurityRole>();
			for (String role : person.roles) {
				u.roles.add(SecurityRole.findByRoleName(role.toLowerCase()));
			}
			u.save();
			u.saveManyToManyAssociations("roles");
			if (u.roles.contains(SecurityRole.findByRoleName("patient"))) {
				User.verify(u);
			} else {
				// invite / verify email address
				final MyUsernamePasswordAuthProvider provider = MyUsernamePasswordAuthProvider.getProvider();
				provider.sendVerifyEmailMailingAfterSignup(u, ctx());
			}
		}
		////
		// FIXME ref to auth user in Person

		Logger.debug(person.toString());
		if (create) {
			person.accountUuid = u.uuid;
			Person.save(person);
		} else {
			// FIXME THIS SEEMS WRONG?:
			Person.find(person.getUUID());
			Person.save(person);

		}
		if (create) {
			response().setHeader(LOCATION, "/v1/persons/" + person.id);
			setHeaders();
			return created();
		} else {
			return okWithHeaders();
		}
	}

	public static Result delete(String id) {
		Logger.debug(id);
		Person.delete(id);
		return okWithHeaders();
	}

//	@Restrict(
//			@Group(Application.USER_ROLE))
	public static Result getAll() {
		Logger.debug("getAll");
		String roleFilter = request().getQueryString("qRole");
		String nameFilter = request().getQueryString("qName");
		Logger.debug("getAll?qName=" + nameFilter + "&qRole=" + roleFilter);
		return okJsonWithHeaders(Person.all(roleFilter, nameFilter));
	}

	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Person.find(id));
	}
}
