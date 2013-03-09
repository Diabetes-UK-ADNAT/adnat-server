package models;

import java.util.List;
import org.bson.types.ObjectId;
import play.data.validation.Constraints.Required;
import com.google.code.morphia.annotations.Entity;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactRequest extends BaseModel {

    @Required
    public String name;
    @Required
    public String email;
    @Required
    public String message;

    public static ContactRequest find(String id) {
        return ds.find(ContactRequest.class).field("_id").equal(new ObjectId(id)).get();
    }

    public static List<ContactRequest> all() {
        return allItems(ContactRequest.class);
    }

    public static void save(ContactRequest item) {
        saveItem(item);
    }

    public static void delete(String idToDelete) {
        deleteItem(ContactRequest.class, find(idToDelete));
    }
}