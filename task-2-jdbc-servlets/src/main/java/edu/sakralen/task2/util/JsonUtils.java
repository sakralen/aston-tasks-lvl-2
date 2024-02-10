package edu.sakralen.task2.util;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import java.io.Reader;
import java.util.Map;

public class JsonUtils {
    private JsonUtils() {
    }

    public static Map<String, Object> toMap(Reader reader) {
        return new GsonBuilder()
                .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create()
                .fromJson(reader, Map.class);
    }
}
