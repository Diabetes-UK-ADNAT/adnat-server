package models;

import java.util.List;
import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.google.code.morphia.query.Query;
import java.util.ArrayList;
import java.util.Date;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import play.data.validation.Constraints;

@Entity(noClassnameStored = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends BaseModel {

	public String name;
	@Constraints.Email
	public String email;
	public String accountUuid; //link to auth User.uuid
	public String password;
	public List<String> roles = new ArrayList<String>();
	@Reference(ignoreMissing = true)
	public Group site;
	public Date agreedToInformationSheet;
	public Date agreedToConsent;
	public Date agreedToAssent;
	@Reference(ignoreMissing = true)
	public List<Person> careTeam = new ArrayList<Person>();
	public Activity activity = new Activity();

	public static Person find(String id) {
		return ds.find(Person.class).field("_id").equal(new ObjectId(id)).get();
	}

	public static List<Person> all(String roleFilter, String nameFilter) {
		Query<Person> q = ds.createQuery(Person.class);
		q.limit(100); //default max limit

		if (nameFilter != null) {
			String[] tokens = nameFilter.replaceAll(",", "").split(" ");
			for (String t : tokens) {
				q.or(
						//q.criteria("name.firstNames").startsWithIgnoreCase(t),
						//q.criteria("name.lastName").startsWithIgnoreCase(t));
						q.criteria("name").containsIgnoreCase(t),
						q.criteria("email").containsIgnoreCase(t));
			}
			q.limit(15);
		}
		if (roleFilter != null) {
			q.field("roles").contains(roleFilter);
		}
		return q.asList();
	}

	public static Person findByAccount(String uuid) {
		Query<Person> q = ds.createQuery(Person.class);
		q.field("accountUuid").equal(uuid);
		return q.get();
	}

	public static void save(Person item) {
		saveItem(item);
	}

	public static void delete(String idToDelete) {
		deleteItem(Person.class, find(idToDelete));
	}

	public static class Activity {

		public Date lastAssessmentPosted;
		public String lastAssessmentUuid;
	}
}