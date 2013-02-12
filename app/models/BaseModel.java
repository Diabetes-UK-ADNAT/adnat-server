package models;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.annotations.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
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
        String created = id != null
                ? ISODateTimeFormat.dateTime()
                .print(new DateTime(new Date(id.getTime())))
                : "null";

        return getClass().getSimpleName().toString()
                + ","
                + id
                + ","
                + created
                + ","
                + ISODateTimeFormat.dateTime().print(new DateTime(updated));
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
