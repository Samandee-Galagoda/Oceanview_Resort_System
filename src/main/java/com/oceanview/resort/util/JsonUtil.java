package com.oceanview.resort.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private JsonUtil() {
    }

    public static String toJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return quote((String) value);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            List<String> parts = new ArrayList<>();
            for (Map.Entry<String, Object> e : map.entrySet()) {
                parts.add(quote(e.getKey()) + ":" + toJson(e.getValue()));
            }
            return "{" + String.join(",", parts) + "}";
        }
        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) value;
            List<String> parts = new ArrayList<>();
            for (Object item : list) {
                parts.add(toJson(item));
            }
            return "[" + String.join(",", parts) + "]";
        }
        return quote(String.valueOf(value));
    }

    public static Map<String, Object> mapOf(String k1, Object v1) {
        return Collections.<String, Object>singletonMap(k1, v1);
    }

    public static String quote(String s) {
        if (s == null) {
            return "null";
        }
        String escaped = s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
        return "\"" + escaped + "\"";
    }
}
