package club.frozed.core.manager.database.redis.payload;

import club.frozed.core.Zoom;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.config.ConfigCursor;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 8/09/2020 @ 19:17
 * Template by Elb1to
 */

public class RedisListener extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                RedisMessage redisMessage = new Gson().fromJson(message,RedisMessage.class);
                switch (redisMessage.getPayload()){
                    case SERVER_MANAGER:{
                        String server = redisMessage.getParam("SERVER");
                        String status = redisMessage.getParam("STATUS");
                        if (status.equalsIgnoreCase("online")) {
                            status = "&aonline";
                        } else {
                            status = "&coffline";
                        }
                        String format = Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("NETWORK.SERVER-MANAGER.FORMAT")
                                .replace("<prefix>", Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("NETWORK.SERVER-MANAGER.PREFIX")))
                                .replace("<server>", server)
                                .replace("<status>", status));
                        StaffLang.sendRedisServerMsg(format);
                        break;
                    }
                    case STAFF_JOIN:{
                        String player = redisMessage.getParam("STAFF");
                        String server = redisMessage.getParam("SERVER");
                        StaffLang.StaffJoinMessage(player, server);
                        break;
                    }
                    case STAFF_LEAVE:{
                        String player = redisMessage.getParam("STAFF");
                        String server = redisMessage.getParam("SERVER");
                        StaffLang.StaffLeaveMessage(player, server);
                        break;
                    }
                    case STAFF_SWITCH:{
                        String player = redisMessage.getParam("STAFF");
                        String last_server = redisMessage.getParam("LAST_SERVER");
                        String actual_server = redisMessage.getParam("ACTUAL_SERVER");
                        StaffLang.StaffSwitchMessage(player, last_server, actual_server);
                        break;
                    }
                    case STAFF_CHAT:{
                        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
                        String server = redisMessage.getParam("SERVER");
                        String player = redisMessage.getParam("PLAYER");
                        String msg = redisMessage.getParam("TEXT");
                        String format = Color.translate(configCursor.getString("FORMAT")
                                .replace("<server>", server)
                                .replace("<player>", player)
                                .replace("<text>", msg));
                        StaffLang.sendStaffChat(format);
                        break;
                    }
                    case ADMIN_CHAT:{
                        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "SETTINGS.ADMIN-CHAT");
                        String server = redisMessage.getParam("SERVER");
                        String player = redisMessage.getParam("PLAYER");
                        String msg = redisMessage.getParam("TEXT");
                        String format = Color.translate(configCursor.getString("FORMAT")
                                .replace("<server>", server)
                                .replace("<player>", player)
                                .replace("<text>", msg));
                        StaffLang.sendAdminChat(format);
                        break;
                    }
                    default:
                        Zoom.getInstance().getLogger().info("[Redis] The message was received, but there was no response");
                        break;
                }
            }
        }.runTask(Zoom.getInstance());
    }
}
