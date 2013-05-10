package models;

import java.util.List;
import org.bson.types.ObjectId;
import play.data.validation.Constraints.Required;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.query.Query;
import java.util.ArrayList;
import java.util.Date;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity(noClassnameStored = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends BaseModel {

	@Required
	public Name name;
	public Date dob;
	@Required
	public ContactInfo contactInfo;
	public Date agreedToTermsAndConditions;
	public Date agreedToPrivacyPolicy;
	public List<String> roles = new ArrayList<String>();
	@Reference(ignoreMissing = true)
	public Group group;

	public static Person find(String id) {
		return ds.find(Person.class).field("_id").equal(new ObjectId(id)).get();
	}

	public static List<Person> all(String roleFilter, String nameFilter) {
		//return ds.createQuery(Person.class).limit(2).asList();
		Query<Person> q = ds.createQuery(Person.class);
		if (nameFilter != null) {
			q.or(
					q.criteria("name.firstNames").containsIgnoreCase(nameFilter),
					q.criteria("name.lastName").containsIgnoreCase(nameFilter));
		}
		if (roleFilter != null) {
			q.field("roles").contains(roleFilter);
		}
		return q.asList();
	}

	public static void save(Person item) {
		saveItem(item);
	}

	public static void delete(String idToDelete) {
		deleteItem(Person.class, find(idToDelete));
	}
}