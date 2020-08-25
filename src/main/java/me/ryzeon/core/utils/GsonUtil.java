package me.ryzeon.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class GsonUtil {

    private JsonObject json = new JsonObject();

    public GsonUtil addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public GsonUtil addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public GsonUtil addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public GsonUtil addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public GsonUtil add(String property, JsonElement element) {
        this.json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return this.json;
    }
}
