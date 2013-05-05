package models;

import java.util.List;
import org.bson.types.ObjectId;
import play.data.validation.Constraints.Required;
import com.google.code.morphia.annotations.Entity;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity(noClassnameStored=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends BaseModel {

    @Required
    public String name;
    
    public static Group find(String id) {
        return ds.find(Group.class).field("_id").equal(new ObjectId(id)).get();
    }

    public static List<Group> all() {
        return allItems(Group.class);
    }

    public static void save(Group item) {
        saveItem(item);
    }

    public static void delete(String idToDelete) {
        deleteItem(Group.class, find(idToDelete));
    }

    @Override
    public String toString() {
        return super.toString()
                + ","
                + name;
    }
}