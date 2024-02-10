package edu.sakralen.task2.util;

public class PathInfoUtils {
    private PathInfoUtils() {
    }

    public static boolean isPathInfoEmpty(String pathInfo) {
        return pathInfo == null || pathInfo.equals("/");
    }

    public static Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
