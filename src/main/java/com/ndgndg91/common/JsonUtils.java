package com.ndgndg91.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonUtils {
    private static final JsonParser parser = new JsonParser();

    public static String parse1Depth(String response, String key){
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get(key).getAsString();
    }

    public static String parse2Depth(String response, String _1depthKey, String _2depthKey){
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get(_1depthKey).getAsJsonObject().get(_2depthKey).getAsString();
    }
}
