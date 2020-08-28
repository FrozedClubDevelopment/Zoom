package club.frozed.zoom.manager.tips;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TipsRunnable extends BukkitRunnable {

    List<String> messageTips = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getStringList("TIPS.TIPS-LIST");
    AtomicInteger normalTip = new AtomicInteger(0);

    @Override
    public void run() {
        String tipsMode = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("TIPS.MODE");
        switch (tipsMode) {
            case "normal":
                if (this.normalTip.get() >= this.messageTips.size()) {
                    this.normalTip.set(0);
                }
                Bukkit.broadcastMessage(Color.translate(this.messageTips.get(this.normalTip.get())));
                this.normalTip.getAndIncrement();
                break;
            case "random":
                Bukkit.broadcastMessage(Color.translate(this.messageTips.get(Utils.randomNumber(0, this.messageTips.size()))));
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§cInvalid mode! Available modes: 'normal' and 'random'");
                break;
        }
    }
}
