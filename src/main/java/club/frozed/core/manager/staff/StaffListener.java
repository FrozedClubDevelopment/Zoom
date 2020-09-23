package club.frozed.core.manager.staff;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffListener implements Listener {

    @EventHandler
    public void onJoinStaffEvent(PlayerJoinEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        if (playerData == null) return;
        if (!playerData.getPlayer().hasPermission("core.staff")) return;
        if (Zoom.getInstance().getRedisManager().isActive()) {
            if (playerData.getLastServer().equals(Lang.SERVER_NAME)) {
                String json = new RedisMessage(Payload.STAFF_JOIN)
                        .setParam("STAFF",e.getPlayer().getName())
                        .setParam("SERVER",Lang.SERVER_NAME).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            } else {
                String json = new RedisMessage(Payload.STAFF_SWITCH)
                        .setParam("STAFF",e.getPlayer().getName())
                        .setParam("LAST_SERVER",playerData.getLastServer())
                        .setParam("ACTUAL_SERVER",Lang.SERVER_NAME).toJSON();
                Zoom.getInstance().getRedisManager().write(json);
            }
        } else {
            StaffLang.StaffJoinMessage(e.getPlayer().getName(), Lang.SERVER_NAME);
        }
    }

    @EventHandler
    public void onLeaveStaffEvent(PlayerQuitEvent e) {
        if (!e.getPlayer().hasPermission("core.staff")) return;
        if (Zoom.getInstance().getRedisManager().isActive()) {
            String json = new RedisMessage(Payload.STAFF_LEAVE)
                    .setParam("STAFF",e.getPlayer().getName())
                    .setParam("SERVER",Lang.SERVER_NAME).toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            StaffLang.StaffLeaveMessage(e.getPlayer().getName(), Lang.SERVER_NAME);
        }
    }
}
