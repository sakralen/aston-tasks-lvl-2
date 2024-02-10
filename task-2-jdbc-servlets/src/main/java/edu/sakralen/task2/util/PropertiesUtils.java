package edu.sakralen.task2.util;

import edu.sakralen.task2.exception.PropertiesLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static final Properties PROPERTIES = new Properties();
    private static final String APP_PROPERTIES_NAME = "application.properties";

    static {
        loadFromResource();
    }

    private PropertiesUtils() {
    }

    public static String getPropertyByName(String propertyName) {
        return PROPERTIES.getProperty(propertyName);
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    private static void loadFromResource() {
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream(APP_PROPERTIES_NAME)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new PropertiesLoadException(e);
        }
    }
}
