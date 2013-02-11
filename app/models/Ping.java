package models;

import java.util.List;
import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ping extends BaseModel {

    public static List<Ping> all() {
        return allItems(Ping.class);
    }

    public static void save(Ping item) {
        saveItem(item);
    }
    
    public static void delete(String idToDelete) {
        deleteItem(Ping.class, find(idToDelete));
    }
    
    public static Ping find(String id) {
        return ds.find(Ping.class).field("_id").equal(new ObjectId(id)).get();
    }

}