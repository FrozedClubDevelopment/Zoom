package me.ryzeon.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {
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

    public static void sendAllMsg(String string) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != null) {
                p.sendMessage(string);
            }
        }
        Bukkit.getConsoleSender().sendMessage(string);
    }

    public static Location teleportToTop(Location loc) {
        return new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
