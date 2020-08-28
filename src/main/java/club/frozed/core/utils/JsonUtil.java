package club.frozed.core.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {

    private JsonObject json = new JsonObject();

    public JsonUtil addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonUtil addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonUtil addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonUtil addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonUtil add(String property, JsonElement element) {
        this.json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return this.json;
    }
}
