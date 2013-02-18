package models;

import java.util.List;
import org.bson.types.ObjectId;
import play.data.validation.Constraints.Required;
import com.google.code.morphia.annotations.Entity;
import java.util.ArrayList;
import java.util.Date;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person extends BaseModel {

    @Required
    public Name name;
    public Date dob;
    @Required
    public ContactInfo contactInfo;
    public Account account;
    public Date agreedToTermsAndConditions;
    public Date agreedToPrivacyPolicy;
    public List<String> roles = new ArrayList<String>();
    public List<String> groups = new ArrayList<String>();

    public static Person find(String id) {
        return ds.find(Person.class).field("_id").equal(new ObjectId(id)).get();
    }

    public static List<Person> all() {
        return allItems(Person.class);
    }

    public static void save(Person item) {
        saveItem(item);
    }

    public static void delete(String idToDelete) {
        deleteItem(Person.class, find(idToDelete));
    }
}