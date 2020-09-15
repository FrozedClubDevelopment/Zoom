package club.frozed.core.manager.tips;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TipsRunnable extends BukkitRunnable {

    List<String> messageTips = Zoom.getInstance().getSettingsConfig().getConfig().getStringList("SETTINGS.TIPS.TIPS-LIST");
    AtomicInteger normalTip = new AtomicInteger(0);

    @Override
    public void run() {
        String tipsMode = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.TIPS.MODE");
        switch (tipsMode) {
            case "normal":
                if (this.normalTip.get() >= this.messageTips.size()) {
                    this.normalTip.set(0);
                }
                Bukkit.broadcastMessage(CC.translate(this.messageTips.get(this.normalTip.get())));
                this.normalTip.getAndIncrement();
                break;
            case "random":
                Bukkit.broadcastMessage(CC.translate(this.messageTips.get(Utils.randomNumber(0, this.messageTips.size()))));
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§cInvalid mode! Available modes: 'normal' and 'random'");
                break;
        }
    }
}
