package models;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import play.Logger;
import play.data.validation.Constraints.Required;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel {

    final static protected Datastore ds = MorphiaObject.datastore;
    @Id
    @JsonIgnore
    public ObjectId id;
    @Required
    public Date updated;

    public String getUUID() {
        return id == null ? null : id.toString();
    }

    @Override
    public String toString() {
        return getUUID()
                + ":"
                + id + ":"
                + updated
                + ":"
                + (id != null ? new Date(id.getTime()) : "null");
    }

    public static <T extends BaseModel> List<T> allItems(Class clazz) {
        if (ds != null) {
            return ds.find(clazz).asList();
        } else {
            return new ArrayList<T>();
        }
    }

    public static <T extends BaseModel> void saveItem(T item) {
        Logger.debug(item.toString());
        item.updated = new Date();
        if (item.id != null) {
            ds.merge(item);
        } else {
            ds.save(item);
        }
    }

    public static <T extends BaseModel> void deleteItem(Class clazz, T item) {
        if (item != null) {
            Logger.info("toDelete: " + item);
            ds.delete(clazz, item.id);
        } else {
            Logger.debug("ID not found");
        }
    }
}
