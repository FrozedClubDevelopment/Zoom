package club.frozed.core.utils.punishment;

import club.frozed.core.manager.player.punishments.Punishment;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bson.Document;

import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 19/10/2020 @ 18:50
 */

public class PunishmentUtil {

    public static Document serialize(Punishment punishment) {
        if (punishment == null) {
            return new Document();
        }

        return punishment.toJSON();
    }

    public static Punishment jsonStringToPunishment(String string){
        Document document = Document.parse(string);

        return new Punishment(document);
    }

    public static Punishment deserialize(Document document) {
        if (document == null || document.isEmpty()) {
            return null;
        }

        return new Punishment(document);
    }
}
