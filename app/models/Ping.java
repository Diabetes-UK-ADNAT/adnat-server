package models;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import play.Logger;
import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

import java.util.Date;

@Entity
public class Ping {

    @Id
    public ObjectId id;
    @Required
    public Date updated;

    public static List<Ping> all() {
        if (MorphiaObject.datastore != null) {
            return MorphiaObject.datastore.find(Ping.class).asList();
        } else {
            return new ArrayList<Ping>();
        }
    }

    public String getUUID() {
        return id == null ? null : id.toString();
    }

    public static void save(Ping ping) {
        Logger.debug(ping.toString());
        if (ping.id != null) {
            MorphiaObject.datastore.merge(ping);
        } else {
            MorphiaObject.datastore.save(ping);
        }
    }

    public static void delete(String idToDelete) {
        Ping toDelete = MorphiaObject.datastore.find(Ping.class).field("_id").equal(new ObjectId(idToDelete)).get();
        if (toDelete != null) {
            Logger.info("toDelete: " + toDelete);
            MorphiaObject.datastore.delete(Ping.class, toDelete.id);
        } else {
            Logger.debug("ID No Found: " + idToDelete);
        }
    }

    public static Ping find(String id) {
        return MorphiaObject.datastore.find(Ping.class).field("_id").equal(new ObjectId(id)).get();
    }

    @Override
    public String toString() {
        return getUUID() + ":" + id + ":" + new Date(id.getTime()) + ":" + updated;
    }
}