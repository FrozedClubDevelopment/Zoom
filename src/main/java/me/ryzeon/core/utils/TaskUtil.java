package me.ryzeon.core.utils;

import me.ryzeon.core.Zoom;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {
    public static void run(Runnable runnable) {
        Zoom.getInstance().getServer().getScheduler().runTask((Plugin) Zoom.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Zoom.getInstance().getServer().getScheduler().runTaskTimer((Plugin) Zoom.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer((Plugin) Zoom.getInstance(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously((Plugin) Zoom.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskLater((Plugin) Zoom.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskLaterAsynchronously((Plugin) Zoom.getInstance(), runnable, delay);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin) Zoom.getInstance(), runnable, 20 * delay, 20 * delay);
    }

    public static void runAsync(Runnable runnable) {
        Zoom.getInstance().getServer().getScheduler().runTaskAsynchronously((Plugin) Zoom.getInstance(), runnable);
    }
}