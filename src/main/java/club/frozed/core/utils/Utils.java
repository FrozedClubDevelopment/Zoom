package club.frozed.core.utils;

import club.frozed.core.Zoom;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.time.DateUtils;
import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static final Type LIST_STRING = new TypeToken<List<String>>() {
    }.getType();

    public static int getPing(Player p) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return -1;
        }
    }

    public static long parse(String source, TimeUnit unit) {
        return TimeUnit.MILLISECONDS.convert(DateUtils.fromString(source), unit);
    }

    public static void globalBroadcast(Player player, String message) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Message");
        output.writeUTF("ALL");
        output.writeUTF(message);

        player.sendPluginMessage(Zoom.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static String getDisplayName(UUID uuid) {
        if (uuid == null) return "Console";
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer != null) {
            return offlinePlayer.getName();
        } else {
            return "Console";
        }
    }

    public static String getStaffName(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getPlayer().getDisplayName() : "§4§lConsole";
    }

    public static String getCommandWithIgnoreArgsOne(String[] args) {
        return Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 0, args.length));
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

    public static boolean hasAvailableSlot(Player player) {
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
        double serverTps = Bukkit.spigot().getTPS()[0];
        tps = decimalFormat.format(serverTps);

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
        if (!(sound.equalsIgnoreCase("none") || sound == null)) {
            p.playSound(p.getLocation(), Sound.valueOf(sound), 2F, 2F);
        }
    }

    public static String timeCalculate(long time) {
        int day = (int) TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minute);
        String hour_text = String.valueOf(hours), minute_text = String.valueOf(minute), second_text = String.valueOf(second);
        if (hours < 10L) hour_text = "0" + hour_text;
        if (minute < 10L) minute_text = "0" + minute_text;
        if (second < 10L) second_text = "0" + second_text;

        return (hours == 0L) ? (minute_text + ":" + second_text) : (hour_text + ":" + minute_text + ":" + second_text);
    }

    public static String formatTimeMillis(long millis) {
        long seconds = millis / 1000L;
        if (seconds <= 0L) return "0 seconds";
        long minutes = seconds / 60L;
        seconds %= 60L;
        long hours = minutes / 60L;
        minutes %= 60L;
        long day = hours / 24L;
        hours %= 24L;
        long years = day / 365L;
        day %= 365L;
        StringBuilder time = new StringBuilder();
        if (years != 0L) time.append(years).append((years == 1L) ? " year " : " years ");
        if (day != 0L) time.append(day).append((day == 1L) ? " day " : " days ");
        if (hours != 0L) time.append(hours).append((hours == 1L) ? " hour " : " hours ");
        if (minutes != 0L) time.append(minutes).append((minutes == 1L) ? " minute " : " minutes ");
        if (seconds != 0L) time.append(seconds).append((seconds == 1L) ? " second " : " seconds ");

        return time.toString().trim();
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) return "now";
        if (toDate.after(fromDate)) future = true;

        StringBuilder sb = new StringBuilder();
        int[] types = {1, 2, 5, 11, 12, 13};
        String[] names = {
                "year", "years",
                "month", "months",
                "day", "days",
                "hour", "hours",
                "minute", "minutes",
                "second", "seconds"
        };

        int accuracy = 0;
        for (int i = 0; i < types.length && accuracy <= 2; i++) {
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + ((diff > 1) ? 1 : 0)]);
            }
        }

        return (sb.length() == 0) ? "now" : sb.toString().trim();
    }

    static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        fromDate.setTimeInMillis(savedDate);
        return --diff;
    }

    public static String buildMessage(String[] args, int start) {
        if (start >= args.length) return "";

        return ChatColor.stripColor(String.join(" ", Arrays.copyOfRange((CharSequence[]) args, start, args.length)));
    }

    public static String getCountry(String ip) throws Exception {
        URL url = new URL("http://ip-api.com/json/" + ip);
        BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder entirePage = new StringBuilder();
        String inputLine;
        while ((inputLine = stream.readLine()) != null) entirePage.append(inputLine);
        stream.close();
        if (!(entirePage.toString().contains("\"country\":\""))) return null;

        return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
    }

    public static boolean checkPlayerVote(UUID uuid) {
        String pageRequest = "https://api.namemc.com/server/" + Lang.SERVER_IP + "/likes?profile=" + uuid.toString();
        try {
            URL url = new URL(pageRequest);
            ArrayList<Object> lines = new ArrayList();
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                lines.add(line);
            if (lines.contains("true")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException exception) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while checking vote on name-mc");
        }

        return false;
    }

    public static String nowDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getUsernameById(int userid) {
        if (Zoom.serverName.equals("%%__USER__%")) {
            return "Robot";
        } else {
            try {
                URL url = new URL("https://www.mc-market.org/members/" + userid);
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String code = "", line = "";
                while ((line = br.readLine()) != null) {
                    code = code + line;
                }
                return code.split("<title>")[1].split("</title>")[0].split(" | ")[0];
            } catch (IOException e) {
            }
        }
        return "NONE";
    }

    public static String getIP() {
        URL url = null;
        BufferedReader in = null;
        String ipAddress = "";
        try {
            url = new URL("http://bot.whatismyipaddress.com");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = in.readLine().trim();
            if (ipAddress.length() <= 0)
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println(ip.getHostAddress().trim());
                    ipAddress = ip.getHostAddress().trim();
                } catch (Exception exp) {
                    ipAddress = "ERROR";
                }
        } catch (Exception ex) {
            Zoom.getInstance().getLogger().info("[License] Error in check your host ip!");
//            Bukkit.getConsoleSender().sendMessage("&c&lYour LocalHost IP are unknown , please check what happend on your intertnet! ERROR LOG:");
            ex.printStackTrace();
        }
        return ipAddress;
    }

    public static int randomNumber(int minimo, int maximo) {
        Random random = new Random();
        int min = Math.min(maximo, maximo);
        int max = Math.max(maximo, maximo);
        int maxsize = min - max;

        return random.nextInt(maxsize + 1) + minimo;
    }
}
