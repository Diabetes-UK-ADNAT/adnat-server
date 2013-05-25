package controllers;

import models.ContactRequest;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import com.typesafe.plugin.*;
import java.util.Date;
import play.Play;

public class ContactRequestController extends BaseController {

	@BodyParser.Of(value = BodyParser.Json.class)
	public static Result save() {
		Logger.debug("save");
		JsonNode json = getJsonFromBody();
		ContactRequest contactRequest = Json.fromJson(json, ContactRequest.class);
		contactRequest.id = getObjectId(json);
		boolean create = contactRequest.id == null;
		ContactRequest.save(contactRequest);
		if (create) {
			sendNotification(contactRequest);
			response().setHeader(LOCATION, "/v1/contactrequests/" + contactRequest.id);
			setHeaders(); //FIXME remove?
			return created();
		} else {
			return okWithHeaders();
		}
	}

	private static void sendNotification(ContactRequest contactRequest) {
		MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
		mail.setSubject("ADNAT Contact Request");
		mail.addRecipient(Play.application().configuration().getString("adnat.email.support"));
		mail.addFrom(Play.application().configuration().getString("adnat.email.support"));
		Date requestDate = new Date();
		String uri = "https://" + request().host() + "/v1/contactrequests/" + contactRequest.getUUID(); //fixme web url
		String html = views.html.email.email_contact.render(uri, requestDate).body();
		String txt = views.txt.email.email_contact.render(uri, requestDate).body();
		mail.send(txt, html);
	}

	public static Result delete(String id) {
		Logger.debug(id);
		ContactRequest.delete(id);
		return okWithHeaders();
	}

	public static Result getAll() {
		Logger.debug("getAll");
		return okJsonWithHeaders(ContactRequest.all());
	}

	public static Result getById(String id) {
		Logger.debug(id);
		return okJsonWithHeaders(ContactRequest.find(id));
	}
}
