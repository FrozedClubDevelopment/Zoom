package me.ryzeon.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public final class GsonUtil {

    public static final Gson NORMAL = new GsonBuilder().disableHtmlEscaping().create();
    public static final Gson PRETTY_PRINTING = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    public static final JsonParser NORMAL_PARSER = new JsonParser();

    public static final Gson PLAIN_GSON = new GsonBuilder().create();
    public static final Type MAP_OBJECT_TYPE = new TypeToken<Map<String, Object>>() {
    }.getRawType();
    public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();
}
