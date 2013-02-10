package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import play.Logger;
import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import controllers.MorphiaObject;

@Entity
public class Faq {

    @Id
    public ObjectId id;
    @Required
    public String question;
    @Required
    public String answer;
    @Required
    public int category;

    public static List<Faq> all() {
        if (MorphiaObject.datastore != null) {
            return MorphiaObject.datastore.find(Faq.class).asList();
        } else {
            return new ArrayList<Faq>();
        }
    }

    public String getUUID() { //fixme BaseModel
        return id == null ? null : id.toString();
    }

    public static void save(Faq faq) {
       Logger.debug(faq.toString());
        if (faq.id != null) {
            MorphiaObject.datastore.merge(faq);
        } else {
            MorphiaObject.datastore.save(faq);
        }

    }

    public static void delete(String idToDelete) {
        Faq toDelete = MorphiaObject.datastore.find(Faq.class).field("_id").equal(new ObjectId(idToDelete)).get();
        if (toDelete != null) {
            Logger.info("toDelete: " + toDelete);
            MorphiaObject.datastore.delete(Faq.class, toDelete.id);
        } else {
            Logger.debug("ID No Found: " + idToDelete);
        }
    }

    public static Faq find(String id) {
        return MorphiaObject.datastore.find(Faq.class).field("_id").equal(new ObjectId(id)).get();
    }

    @Override
    public String toString() {
        return getUUID() + ":" + id + ":" + category + ":" + question + ":" + answer;
    }
}