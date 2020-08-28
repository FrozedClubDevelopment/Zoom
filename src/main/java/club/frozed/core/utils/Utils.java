package club.frozed.core.utils;

import club.frozed.core.Zoom;
import club.frozed.core.utils.items.ItemCreator;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    public static void globalBroadcast(Player player, String message) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Message");
        output.writeUTF("ALL");
        output.writeUTF(message);

        player.sendPluginMessage(Zoom.getInstance(), "BungeeCord", output.toByteArray());
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

    public static String timeCalculate(long time) {
        int day = (int) TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
        String hour_text = String.valueOf(hours), minute_text = String.valueOf(minute), second_text = String.valueOf(second);
        if (hours < 10L)
            hour_text = "0" + hour_text;
        if (minute < 10L)
            minute_text = "0" + minute_text;
        if (second < 10L)
            second_text = "0" + second_text;
        return (hours == 0L) ? (minute_text + ":" + second_text) : (hour_text + ":" + minute_text + ":" + second_text);
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

    public static int randomNumber(int minimo, int maximo) {
        Random random = new Random();
        int min = Math.min(maximo, maximo);
        int max = Math.max(maximo, maximo);
        int maxsize = min - max;
        return random.nextInt(maxsize + 1) + minimo;
    }

}
