package com.units.it.helpers;

import com.googlecode.objectify.Key;
import com.units.it.utils.Consts;

import static com.googlecode.objectify.ObjectifyService.ofy;

public abstract class AbstractHelper {

    public static <T> void saveDelayed(T t) {
        ofy().save().entity(t);
    }

    public static <T> void saveNow(T t) {
        ofy().save().entity(t).now();
    }

    public static <T> void deleteEntity(T t) {
        ofy().delete().entity(t);
    }

    public static <T> T getById(Class<T> c, String id) {
        try {
            Key<T> k = Key.create(c, id);
            return ofy().load().key(k).now();
        } catch (Exception e) {
            if (Consts.debug) {
                System.out.println("ERR - Not Found (" + id + ")\n");
            }
            return null;
        }
    }
}