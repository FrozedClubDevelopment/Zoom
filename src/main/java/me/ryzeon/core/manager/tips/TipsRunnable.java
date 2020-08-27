package me.ryzeon.core.manager.tips;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TipsRunnable extends BukkitRunnable {
    List<String> msg = Zoom.getInstance().getSettingsconfig().getConfig().getStringList("tips.tips");
    AtomicInteger tipnormal = new AtomicInteger(0);

    @Override
    public void run() {
        String modo = Zoom.getInstance().getSettingsconfig().getConfig().getString("tips.mode");
        switch (modo) {
            case "normal":
                if (this.tipnormal.get() >= this.msg.size()) {
                    this.tipnormal.set(0);
                }
                Bukkit.broadcastMessage(Color.translate(this.msg.get(this.tipnormal.get())));
                this.tipnormal.getAndIncrement();
                break;
            case "random":
                Bukkit.broadcastMessage(Color.translate(this.msg.get(Utils.randomNumber(0, this.msg.size()))));
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§cPlease check your config :)");
                break;
        }
    }
}
