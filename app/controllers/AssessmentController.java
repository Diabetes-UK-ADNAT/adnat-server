package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Assessment;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import com.typesafe.plugin.*;
import java.util.Date;
import models.Person;
import models.auth.User;
import play.Play;
import providers.MyUsernamePasswordAuthProvider;

public class AssessmentController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
//	@Restrict(
//			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result save() {
		Logger.debug("save");
		Logger.debug(request().body().toString());
		JsonNode json = getJsonFromBody();
		Assessment assessment = Json.fromJson(json, Assessment.class);
		assessment.id = getObjectId(json);
		Logger.debug(assessment.userToken);
		// users who post assessments can't login to site so this is ok; 
		// also, tokens set by touch login only for now
		User u = User.findByLastLoginToken(assessment.userToken);
		Logger.debug(u.uuid);
		assessment.person = Person.findByAccount(u.uuid);
		Logger.debug(assessment.person.toString());
		boolean create = assessment.id == null;
		Assessment.save(assessment);
		assessment.person.activity.lastAssessmentPosted = new Date();
		assessment.person.activity.lastAssessmentUuid = assessment.getUUID();
		Person.save(assessment.person);
		if (create) {
			sendNotification(assessment);
			response().setHeader(LOCATION, "/v1/assessments/" + assessment.id);
			setHeaders();
			return created();
		} else {
			// really only for creates now; revisit user logic when use for updates
			return okWithHeaders();
		}
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result delete(String id) {
		Logger.debug(id);
		Assessment.delete(id);
		return okWithHeaders();
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE_ADMIN))
	public static Result getAll() {
		Logger.debug("getAll");
		return okJsonWithHeaders(Assessment.all());
	}

	@Restrict(
			@Group(MyUsernamePasswordAuthProvider.USER_ROLE))
	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Assessment.find(id));
	}

	private static void sendNotification(Assessment assessment) {
		// body is same for all so just create once 
		Date notificationDate = new Date();
		String uri = "https://" + request().host() + "/#/assessment/view/" + assessment.getUUID();
		uri = uri.replace("api.", "");
		String patient = assessment.person.email;
		String html = views.html.email.email_assessment_notification.render(patient, uri, notificationDate).body();
		String txt = views.txt.email.email_assessment_notification.render(patient, uri, notificationDate).body();

		// headers
		Person px = Person.findByAccount(assessment.person.accountUuid);
		for (Person p : px.careTeam) {
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
			mail.addFrom(Play.application().configuration().getString("adnat.email.support"));
			mail.setSubject("ADNAT Assessment Posted");
			mail.addRecipient(p.email);
			mail.send(txt, html);
		}
	}
}
