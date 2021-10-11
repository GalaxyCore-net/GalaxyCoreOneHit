package net.galaxycore.onehit.utils;

import java.util.Objects;

public class ObjectHelpers {
    public static <T> T objectOrDefault(T object, T def) {
        if (Objects.isNull(object))
            return def;
        return object;
    }
}
