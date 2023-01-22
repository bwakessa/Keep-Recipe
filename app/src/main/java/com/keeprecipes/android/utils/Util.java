package com.keeprecipes.android.utils;

import java.util.Collection;

public class Util {
    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }
}
