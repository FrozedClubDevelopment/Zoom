package me.ryzeon.core.manager.tips;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TipsRunnable extends BukkitRunnable {
    List<String> msg = Zoom.getInstance().getSettingsconfig().getConfig().getStringList("tips.tips");
    Random random = new Random();
    int tip = random.nextInt(msg.size() - 1);
    AtomicInteger tipnormal = new AtomicInteger(0);

    @Override
    public void run() {
        String modo = Zoom.getInstance().getSettingsconfig().getConfig().getString("tips.mode");
        switch (modo) {
            case "normal":
                Bukkit.broadcastMessage(Color.translate(this.msg.get(this.tipnormal.get())));
                this.tipnormal.getAndIncrement();
                if (this.tipnormal.get() > this.msg.size()) {
                    this.tipnormal.set(0);
                }
                break;
            case "random":
                Bukkit.broadcastMessage(Color.translate(this.msg.get(this.tip)));
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§cPlease check your config :)");
                break;
        }
    }
}
