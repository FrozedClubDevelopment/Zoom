package club.frozed.core.utils.punishment;

import club.frozed.core.manager.player.punishments.Punishment;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 13:56
 */

public class PunishmentAdapter implements JsonDeserializer<Punishment>, JsonSerializer<Punishment> {

    @Override
    public JsonElement serialize(Punishment src, Type typeOfSrc, JsonSerializationContext context) {
        return src.toJson();
    }

    @Override
    public Punishment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Punishment(json.getAsJsonObject());
    }

}