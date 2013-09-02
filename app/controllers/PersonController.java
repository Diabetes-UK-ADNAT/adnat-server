package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import models.PassTest;
import models.Person;
import models.auth.SecurityRole;
import models.auth.TokenAction;
import models.auth.User;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import static play.mvc.Controller.ctx;
import play.mvc.Result;
import providers.MyLoginUsernamePasswordAuthUser;
import providers.MyUsernamePasswordAuthProvider;
import providers.MyUsernamePasswordAuthUser;

public class PersonController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result testpw() {
		Logger.debug("testpw");
		JsonNode json = getJsonFromBody();
		PassTest passTest = Json.fromJson(json, PassTest.class);
		final MyLoginUsernamePasswordAuthUser authUser = new MyLoginUsernamePasswordAuthUser(passTest.password, passTest.email);
		String authResult = MyUsernamePasswordAuthProvider.testpw(authUser);
		Logger.info(authResult);
		if (authResult.equals("VALID")) {
			return okWithHeaders();
		} else {
			throw new RuntimeException(MessageFormat.format("Invalid login {0}", new Object[]{authResult}));
		}
	}

	@BodyParser.Of(value = BodyParser.Json.class)
	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result save() {
		Logger.debug("save");
		JsonNode json = getJsonFromBody();
		// get person 
		Person person = Json.fromJson(json, Person.class);
		person.id = getObjectId(json);
		boolean create = person.id == null;

		saveSite(person, json);
		saveCareTeam(person, json);
		User u = saveUser(person, create);
		savePerson(person, create, u);

		if (create) {
			response().setHeader(LOCATION, "/v1/persons/" + person.id);
			setHeaders();
			return created();
		} else {
			return okWithHeaders();
		}
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result delete(String id) {
		Logger.debug(id);
		Person p = Person.find(id);
		User u = User.findByEmail(p.email);
		TokenAction.deleteByUser(u, TokenAction.Type.PASSWORD_RESET);
		TokenAction.deleteByUser(u, TokenAction.Type.EMAIL_VERIFICATION);
		u.deleteManyToManyAssociations("roles");
		u.delete();
		Person.delete(id);
		return okWithHeaders();
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result getAll() {
		Logger.debug("getAll");
		String roleFilter = request().getQueryString("qRole");
		String nameFilter = request().getQueryString("qName");
		Logger.debug("getAll?qName=" + nameFilter + "&qRole=" + roleFilter);
		return okJsonWithHeaders(Person.all(getSubject(), roleFilter, nameFilter));
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Person.find(id));
	}

//	@SubjectPresent
	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result subject() {
		return okJsonWithHeaders(getSubject());
	}

	private static User createUserAccount(Person person) {
		MyUsernamePasswordAuthProvider.MySignup signup = new MyUsernamePasswordAuthProvider.MySignup();
		signup.email = person.email;
		signup.password = person.password;
		signup.repeatPassword = person.password;
		signup.name = signup.email;
		MyUsernamePasswordAuthUser user = new MyUsernamePasswordAuthUser(signup);
		return User.create(user);
	}

	private static void updateUserAccount(Person person, User u) {
		if (person.password != null) {
			u.changePassword(new MyUsernamePasswordAuthUser(person.password), true);
			u.save();
		}
		u.deleteManyToManyAssociations("roles");
		for (String role : person.roles) {
			Logger.debug(role);
			u.roles.add(SecurityRole.findByRoleName(role.toLowerCase()));
		}
		u.saveManyToManyAssociations("roles");
	}

	private static User setupNewUser(Person person, User u) {
		if (person.password == null) {
			person.password = UUID.randomUUID().toString();
		}
		u = createUserAccount(person);
		for (String role : person.roles) {
			SecurityRole newRole = SecurityRole.findByRoleName(role.toLowerCase());
			u.roles.add(newRole);
		}
		u.save();
		u.saveManyToManyAssociations("roles");
		if (u.roles.contains(SecurityRole.findByRoleName("patient"))) {
			User.verify(u);
		} else {
			// RESET SIGN UP add verify, we are just going to ask them to reset their new password
			User.verify(u);
			// invite / verify email address
			final MyUsernamePasswordAuthProvider provider = MyUsernamePasswordAuthProvider.getProvider();
//				provider.sendVerifyEmailMailingAfterSignup(u, ctx()); // RESET SIGN UP
			provider.sendPasswordResetMailing(u, ctx()); // RESET SIGN UP
		}
		return u;
	}

	private static void savePerson(Person person, boolean create, User u) {
		person.password = null; //saved in auth system (roles are in both for ease of querying person by role)
		Logger.debug(person.toString());
		if (create) {
			person.accountUuid = u.uuid;
			person.roles.add(MyUsernamePasswordAuthProvider.USER_ROLE);
			Person.save(person);
		} else {
			// FIXME THIS SEEMS WRONG?:
			Person.find(person.getUUID());
			Person.save(person);

		}
	}

	private static User saveUser(Person person, boolean create) {
		//PlayAuthenticate.storeUser(session(), authUser);
		//User u = User.findByUsernamePasswordIdentity(user);
		User u = User.findByEmail(person.email);

		if (u != null) {
			if (create) {
				throw new IllegalArgumentException(MessageFormat.format("Duplicate email address {0}", new Object[]{person.email}));
			}
			updateUserAccount(person, u);
		} else {
			u = setupNewUser(person, u);
		}
		return u;
	}

	private static void saveCareTeam(Person person, JsonNode json) {
		person.careTeam = new ArrayList();
		Iterator<JsonNode> teamNodes = json.findPath("careTeam").getElements();
		while (teamNodes.hasNext()) {
			JsonNode n = teamNodes.next();
			Person p = Json.fromJson(n, Person.class);
			p.id = new ObjectId(n.get("uuid").getTextValue());
			person.careTeam.add(p);
		}
	}

	private static void saveSite(Person person, JsonNode json) {
		// handle referenced objects
		if (person.site == null) { //fixme bogus site example
			// error required field
		} else {
			person.site.id = new ObjectId(json.findPath("site").findPath("uuid").getTextValue());
			models.Group.save(person.site);
		}
	}
}
