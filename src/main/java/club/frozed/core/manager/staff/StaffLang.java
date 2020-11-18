package club.frozed.core.manager.staff;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffLang {

    public static void StaffJoinMessage(String player, String server) {
        String format = Zoom.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.STAFF-ALERTS.JOIN")
                .replace("<player>", player)
                .replace("<server>", server
        );

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(CC.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate(format));
    }

    public static void StaffLeaveMessage(String player, String server) {
        String format = Zoom.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.STAFF-ALERTS.QUIT")
                .replace("<player>", player)
                .replace("<server>", server
        );

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(CC.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate(format));
    }

    public static void StaffSwitchMessage(String player, String lastserver, String actualserver) {
        String format = Zoom.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.STAFF-ALERTS.SWITCH")
                .replace("<player>", player)
                .replace("<server>", actualserver)
                .replace("<lastServer>", lastserver
        );
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("core.staff.join")) {
                p.sendMessage(CC.translate(format));
            }
        }
        Bukkit.getConsoleSender().sendMessage(CC.translate(format));
    }

    public static void sendRedisServerMsg(String string) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("core.servermanager")) {
                p.sendMessage(string);
            }
        }
        Bukkit.getConsoleSender().sendMessage(string);
    }

    public static void sendAdminChat(String msg) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.hasPermission("core.adminchat")) return;
            p.sendMessage(msg);
        }
    }

    public static void sendStaffChat(String msg) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.hasPermission("core.staffchat")) return;
            p.sendMessage(msg);
        }
    }

    public static void sendReport(String sender, String target, String server, String reason) {
        String format = Zoom.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.REPORT.MSG.STAFF")
                .replace("<server>", server)
                .replace("<sender>", sender)
                .replace("<target>", target)
                .replace("<reason>", reason);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.hasPermission("core.staff.report")) return;
            p.sendMessage(CC.translate(format));
        }
    }
}
