package me.ryzeon.core.manager.database.redis.handler;

import com.google.gson.JsonObject;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.database.redis.manager.JedisSubscriptionHandler;
import me.ryzeon.core.manager.staff.StaffLang;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.config.ConfigCursor;

public class RedisSubHandler implements JedisSubscriptionHandler {
    String prefix = Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("server.prefix"));

    @Override
    public void handleMessage(JsonObject json) {
        Payload payload;
        try {
            payload = Payload.valueOf(json.get("payload").getAsString());
        } catch (IllegalArgumentException e) {
            Zoom.getInstance().getLogger().warning("Received a payload-type that could not be parsed");
            return;
        }
        JsonObject data = json.get("data").getAsJsonObject();
        switch (payload) {
            case SERVER_MANAGER: {
                String server = data.get("SERVER").getAsString();
                String status = data.get("STATUS").getAsString();
                if (status.equalsIgnoreCase("online")) {
                    status = "&aonline";
                } else {
                    status = "&coffline";
                }
                String format = Color.translate(Zoom.getInstance().getMessagesconfig().getConfig().getString("server.format")
                        .replace("<prefix>", this.prefix)
                        .replace("<server>", server)
                        .replace("<status>", status));
                StaffLang.sendRedisServerMsg(format);
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
                StaffLang.sendAdminChat(format);
                break;
            }
            case STAFF_CHAT: {
                ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat.staff-chat");
                String server = data.get("SERVER").getAsString();
                String player = data.get("PLAYER").getAsString();
                String msg = data.get("TEXT").getAsString();
                String format = Color.translate(configCursor.getString("format")
                        .replace("<server>", server)
                        .replace("<player>", player)
                        .replace("<text>", msg));
                StaffLang.sendStaffChat(format);
                break;
            }
            case STAFF_JOIN: {
                String player = data.get("STAFF").getAsString();
                String server = data.get("SERVER").getAsString();
                StaffLang.StaffJoinMessage(player, server);
                break;
            }
            case STAFF_LEAVE: {
                String player = data.get("STAFF").getAsString();
                String server = data.get("SERVER").getAsString();
                StaffLang.StaffLeaveMessage(player, server);
                break;
            }
            case STAFF_SWICTH: {
                String player = data.get("STAFF").getAsString();
                String last_server = data.get("LAST_SERVER").getAsString();
                String actual_server = data.get("ACTUAL_SERVER").getAsString();
                StaffLang.StaffSwitchMessage(player, last_server, actual_server);
                break;
            }
        }
    }
}
