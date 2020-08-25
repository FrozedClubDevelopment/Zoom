package me.ryzeon.core.utils;

import me.ryzeon.core.utils.items.ItemCreator;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.text.DecimalFormat;

public class Utils {
    public static int getPing(Player p) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            return ((Integer) handle.getClass().getDeclaredField("ping").get(handle)).intValue();
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

    public static boolean hasAvaliableSlot(Player player) {
        Inventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    public static String getTps() {
        String tps;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        double servertps = Bukkit.spigot().getTPS()[0];
        tps = decimalFormat.format(servertps);
        return tps;
    }

    public static String getUptime() {
        long serverTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        String text;
        text = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - serverTime, true, true);
        return text;
    }

    public static long getMaxMemory() {
        long text;
        text = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        return text;
    }

    public static long getAllMemory() {
        long text;
        text = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        return text;
    }

    public static long getFreeMemory() {
        long text;
        text = Runtime.getRuntime().freeMemory() / 1024 / 1024;
        return text;
    }

    public static void sendPlayerSound(Player p, String sound) {
        if (!(sound.equals("none") || sound.equals("NONE") || sound == null)) {
            p.playSound(p.getLocation(), Sound.valueOf(sound), 2F, 2F);
        }
    }

    public static String getCountry(String ip) throws Exception {
        URL url = new URL("http://ip-api.com/json/" + ip);
        BufferedReader stream = new BufferedReader(new InputStreamReader(
                url.openStream()));
        StringBuilder entirePage = new StringBuilder();
        String inputLine;
        while ((inputLine = stream.readLine()) != null)
            entirePage.append(inputLine);
        stream.close();
        if (!(entirePage.toString().contains("\"country\":\"")))
            return null;
        return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
    }

    public static void fillInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) {
                inventory.setItem(i, new ItemCreator(Material.STAINED_GLASS_PANE, 7).setName(" ").get());
            }
        }
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
}
