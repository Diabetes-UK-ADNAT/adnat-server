package models;

import java.util.List;
import org.bson.types.ObjectId;
import play.data.validation.Constraints.Required;
import com.google.code.morphia.annotations.Entity;
import java.util.ArrayList;
import static models.BaseModel.ds;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity(noClassnameStored=true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content extends BaseModel {

    @Required
    public String title;
    @Required
    public String value;
    
    public static Content find(String id) {
        return ds.find(Content.class).field("_id").equal(new ObjectId(id)).get();
    }

    public static List<Content> all() {
        return allItems(Content.class);
    }

    public static void save(Content item) {
        saveItem(item);
    }

    public static void delete(String idToDelete) {
        deleteItem(Content.class, find(idToDelete));
    }

    @Override
    public String toString() {
        return super.toString()
                + ","
                + title
                + ","
                + value;
    }
}