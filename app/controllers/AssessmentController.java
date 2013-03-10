package controllers;

import models.Assessment;
import org.codehaus.jackson.JsonNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;

public class AssessmentController extends BaseController {

    @BodyParser.Of(value = BodyParser.Json.class)
    public static Result save() {
        Logger.debug("save");
        Logger.debug(request().body().toString());
        JsonNode json = getJsonFromBody();
        Assessment assessment = Json.fromJson(json, Assessment.class);
        assessment.id = getObjectId(json);
        boolean create = assessment.id == null;
        Assessment.save(assessment);
        if (create) {
            // fixme url /v1/assessments/
            response().setHeader(LOCATION, "/v1/assessments/" + assessment.id + ".json");
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
}
