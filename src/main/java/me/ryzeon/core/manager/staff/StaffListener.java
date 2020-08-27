package me.ryzeon.core.manager.staff;

import me.ryzeon.core.manager.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffListener implements Listener {

    @EventHandler
    public void onJoinStaffEvent(PlayerJoinEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        if (playerData == null) return;
//        if (Zoom.getInstance().getRedis().isActive()) {
//            if (playerData.getLastserver().equals(Lang.SERVER_NAME)) {
//                Zoom.getInstance().getRedis().write("STAFF_JOIN", new JsonUtil()
//                        .addProperty("STAFF", e.getPlayer().getName())
//                        .addProperty("SERVER", Lang.SERVER_NAME).get());
//            } else {
//                Zoom.getInstance().getRedis().write("STAFF_SWICTH", new JsonUtil()
//                        .addProperty("STAFF", e.getPlayer().getName())
//                        .addProperty("LAST_SERVER", playerData.getLastserver())
//                        .addProperty("ACTUAL_SERVER", Lang.SERVER_NAME).get());
//            }
//        } else {
//            StaffLang.StaffJoinMessage(e.getPlayer().getName(), Lang.SERVER_NAME);
//        }
    }

    @EventHandler
    public void onLeaveStaffEvent(PlayerQuitEvent e) {
//        if (Zoom.getInstance().getRedis().isActive()) {
//            Zoom.getInstance().getRedis().write("STAFF_LEAVE", new JsonUtil()
//                    .addProperty("STAFF", e.getPlayer().getName())
//                    .addProperty("SERVER", Lang.SERVER_NAME).get());
//        } else {
//            StaffLang.StaffLeaveMessage(e.getPlayer().getName(), Lang.SERVER_NAME);
//        }
    }
}
