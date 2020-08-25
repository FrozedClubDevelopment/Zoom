package me.ryzeon.core.manager.staff;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffLang {

    public static void StaffJoinMessage(String player, String server) {
        String format = Zoom.getInstance().getMessagesconfig().getConfig().getString("staff.join").replace("<player>", player).replace("<server>", server);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(Color.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(Color.translate(format));
    }

    public static void StaffLeaveMessage(String player, String server) {
        String format = Zoom.getInstance().getMessagesconfig().getConfig().getString("staff.quit").replace("<player>", player).replace("<server>", server);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(Color.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(Color.translate(format));
    }

    public static void StaffSwitchMessage(String player, String lastserver, String actualserver) {
        String format = Zoom.getInstance().getMessagesconfig().getConfig().getString("staff.switch")
                .replace("<player>", player)
                .replace("<server>", actualserver)
                .replace("<lastserver>", lastserver);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(Color.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(Color.translate(format));
    }

    public static void sendRedisServerMsg(String string) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("core.servermanager")) {
                p.sendMessage(string);
            }
        }
        Bukkit.getConsoleSender().sendMessage(string);
    }

    public static void sendAdminChat(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("core.adminchat")) return;
            p.sendMessage(msg);
        }
    }

    public static void sendStaffChat(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("core.adminchat")) return;
            p.sendMessage(msg);
        }
    }
}
