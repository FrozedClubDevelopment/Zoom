package me.ryzeon.core.manager.database.redis.handler;

import com.google.gson.JsonObject;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.jedis.JedisSubscriptionHandler;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.config.ConfigCursor;

public class RedisMessagesHandler implements JedisSubscriptionHandler {
    @Override
    public void handleMessage(JsonObject json) {
        String prefix = Color.translate("&7[&eServerManager&7]");
        Action action = Action.valueOf(json.get("action").getAsString());
        JsonObject data = json.get("data").isJsonNull() ? null : json.get("data").getAsJsonObject();
        switch (action) {
            case SERVER_ON:
            case SERVER_OFF: {
                ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "server");
                String server = data.get("SERVER").getAsString();
                String status = data.get("status").getAsString();
                Utils.sendRedisServerMsg(Color.translate(server + " " + status));
                break;
            }
            case ADMIN_CHAT: {
                ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.admin-chat");
                String server = data.get("SERVER").getAsString();
                String player = data.get("PLAYER").getAsString();
                String msg = data.get("TEXT").getAsString();
                String format = Color.translate(configCursor.getString("format")
                        .replace("<server>", server)
                        .replace("<player>", player)
                        .replace("<text>", msg));
                Utils.sendAdminChat(format);
                break;
            }
        }
    }
}
