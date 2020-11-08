package club.frozed.core.utils;

import club.frozed.core.Zoom;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        Zoom.getInstance().getServer().getScheduler().runTask(Zoom.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Zoom.getInstance().getServer().getScheduler().runTaskTimer(Zoom.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Zoom.getInstance(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(Zoom.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskLater(Zoom.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Zoom.getInstance(), runnable, delay);
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        Zoom.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Zoom.getInstance(), runnable, 20 * delay, 20 * delay);
    }

    public static void runAsync(Runnable runnable) {
        Zoom.getInstance().getServer().getScheduler().runTaskAsynchronously(Zoom.getInstance(), runnable);
    }
}