package me.ryzeon.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    public static String format(String format, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(format
                .replace("$1", "%1$s")
                .replace("$2", "%2$s")
                .replace("$3", "%3$s")
                .replace("$4", "%4$s")
                .replace("$5", "%5$s")
                .replace("$6", "%6$s")
                .replace("$7", "%7$s")
                .replace("$8", "%8$s")
                .replace("$9", "%9$s")
                .replace("$10", "%10$s"), args));
    }
    public static int getPing(Player p){
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            return ((Integer)handle.getClass().getDeclaredField("ping").get(handle)).intValue();
        } catch (Exception e) {
            return -1;
        }
    }
    public static void sendAllMsg(String string){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p != null){
                p.sendMessage(string);
            }
        }
        Bukkit.getConsoleSender().sendMessage(string);
    }
}
