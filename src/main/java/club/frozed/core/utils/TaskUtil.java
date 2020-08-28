package club.frozed.zoom.utils;

import club.frozed.zoom.ZoomPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {
    public static void run(Runnable runnable) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTask((Plugin) ZoomPlugin.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTaskTimer((Plugin) ZoomPlugin.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer((Plugin) ZoomPlugin.getInstance(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously((Plugin) ZoomPlugin.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTaskLater((Plugin) ZoomPlugin.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTaskLaterAsynchronously((Plugin) ZoomPlugin.getInstance(), runnable, delay);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin) ZoomPlugin.getInstance(), runnable, 20 * delay, 20 * delay);
    }

    public static void runAsync(Runnable runnable) {
        ZoomPlugin.getInstance().getServer().getScheduler().runTaskAsynchronously((Plugin) ZoomPlugin.getInstance(), runnable);
    }
}