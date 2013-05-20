package controllers;

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

public class AssessmentController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
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
		assessment.person = Person.findByAccount(u.uuid);
		Logger.debug(assessment.person.toString());
		boolean create = assessment.id == null;
		Assessment.save(assessment);
		if (create) {
			sendNotification(assessment);
			response().setHeader(LOCATION, "/v1/assessments/" + assessment.id);
			setHeaders();
			return created();
		} else {
			return okWithHeaders();
		}
	}

	public static Result delete(String id) {
		Logger.debug(id);
		Assessment.delete(id);
		return okWithHeaders();
	}

	public static Result getAll() {
		Logger.debug("getAll");
		return okJsonWithHeaders(Assessment.all());
	}

	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(Assessment.find(id));
	}

	private static void sendNotification(Assessment assessment) {
		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		mail.setSubject("ADNAT Assessment Posted");
//FIXME add assessment notification recipients based on patient care team
		mail.addRecipient("ADNAT Support <support@myadnat.co.uk>", "support@myadnat.co.uk");
		mail.addRecipient("Eric Link <ericmlink@gmail.com>", "ericmlink@gmail.com");

		mail.addFrom("ADNAT Support <support@myadnat.co.uk>");

		Date notificationDate = new Date();
		String uri = "https://" + request().host() + "/v1/assessments/" + assessment.getUUID(); //fixme web url
		String patient = "patient name"; //fixme patient email address here
		String html = views.html.email.email_assessment_notification.render(patient, uri, notificationDate).body();
		String txt = views.txt.email.email_assessment_notification.render(patient, uri, notificationDate).body();
		mail.send(txt, html);
	}
}
