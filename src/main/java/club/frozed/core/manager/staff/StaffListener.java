package club.frozed.core.manager.staff;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.lang.Lang;
import club.frozed.lib.task.TaskUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinStaffEvent(PlayerJoinEvent e) {
        TaskUtil.runAsync(() -> {
            PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
            if (playerData == null) return;
            if (!playerData.getPlayer().hasPermission("core.staff")) return;
            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json;
                String lastServer = playerData.getLastServer() == null ? Lang.SERVER_NAME : playerData.getLastServer();
                if (lastServer.equals(Lang.SERVER_NAME)) {
                    json = new RedisMessage(Payload.STAFF_JOIN)
                            .setParam("STAFF", e.getPlayer().getDisplayName())
                            .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                } else {
                    long time = System.currentTimeMillis() - playerData.getLastServerTime();
                    if (time < (5 * 1000)) { // 5 segundos p cambiar de server
                        json = new RedisMessage(Payload.STAFF_SWITCH)
                                .setParam("STAFF", e.getPlayer().getDisplayName())
                                .setParam("LAST_SERVER", playerData.getLastServer())
                                .setParam("ACTUAL_SERVER", Lang.SERVER_NAME).toJSON();
                    } else {
                        json = new RedisMessage(Payload.STAFF_JOIN)
                                .setParam("STAFF", e.getPlayer().getDisplayName())
                                .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                    }
                }
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.StaffJoinMessage(e.getPlayer().getDisplayName(), Lang.SERVER_NAME);
            }
        });
    }

    @EventHandler
    public void onLeaveStaffEvent(PlayerQuitEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) return;
        if (!playerData.getPlayer().hasPermission("core.staff")) return;
        TaskUtil.runAsync(() -> {
            if (Zoom.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.STAFF_LEAVE)
                        .setParam("STAFF", playerData.getHighestRank().getPrefix() + e.getPlayer().getName())
                        .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.StaffLeaveMessage(e.getPlayer().getDisplayName(), Lang.SERVER_NAME);
            }
        });
    }
}
