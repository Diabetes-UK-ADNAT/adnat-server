package models;

import java.util.ArrayList;
import java.util.Date;
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
    public String id;
    @Required
    public String question;
    @Required
    public String answer;

    public static List<Faq> all() {
        if (MorphiaObject.datastore != null) {
            return MorphiaObject.datastore.find(Faq.class).asList();
        } else {
            return new ArrayList<Faq>();
        }
    }

    public static void create(Faq faq) {
        MorphiaObject.datastore.save(faq);
    }

    public static void delete(String idToDelete) {
        Faq toDelete = MorphiaObject.datastore.find(Faq.class).field("_id").equal(new ObjectId(idToDelete)).get();
        if (toDelete != null) {
            Logger.info("toDelete: " + toDelete);
            MorphiaObject.datastore.delete(toDelete);
        } else {
            Logger.debug("ID No Found: " + idToDelete);
        }
    }

    public static Faq find(String id) {
        return MorphiaObject.datastore.find(Faq.class).field("_id").equal(new ObjectId(id)).get();
    }

    @Override
    public String toString() {
        return id + ":" + question + ":" + answer;
    }
}