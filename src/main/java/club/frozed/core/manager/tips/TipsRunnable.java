package club.frozed.core.manager.tips;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TipsRunnable extends BukkitRunnable {

    List<String> messageTips = Zoom.getInstance().getSettingsConfig().getConfiguration().getStringList("SETTINGS.TIPS.TIPS-LIST");
    AtomicInteger normalTip = new AtomicInteger(0);

    @Override
    public void run() {
        String tipsMode = Zoom.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.TIPS.MODE");
        switch (tipsMode) {
            case "normal":
                if (this.normalTip.get() >= this.messageTips.size()) {
                    this.normalTip.set(0);
                }
                String tip = this.messageTips.get(this.normalTip.get());
                if (tip.contains("{C}")) {
                    tip = tip.replace("{C}", "");
                    Bukkit.broadcastMessage(CC.translate(CC.getCenteredMessage(tip).replace("{0}", "\n")));
                } else {
                    Bukkit.broadcastMessage(CC.translate(tip.replace("{0}", "\n")));
                }
//                Bukkit.broadcastMessage(CC.translate(this.messageTips.get(this.normalTip.get()).replace("{0}", "\n")));
                this.normalTip.getAndIncrement();
                break;
            case "random":
                String RandomTip = this.messageTips.get(Utils.randomNumber(0, this.messageTips.size()));
                if (RandomTip.contains("{C}")) {
                    RandomTip = RandomTip.replace("{C}", "");
                    Bukkit.broadcastMessage(CC.translate(CC.getCenteredMessage(RandomTip).replace("{0}", "\n")));
                } else {
                    Bukkit.broadcastMessage(CC.translate(RandomTip.replace("{0}", "\n")));
                }
//                Bukkit.broadcastMessage(CC.translate(this.messageTips.get(Utils.randomNumber(0, this.messageTips.size()))).replace("{0}", "\n"));
                break;
            default:
                Bukkit.getConsoleSender().sendMessage("§cInvalid mode! Available modes: 'normal' and 'random'");
                break;
        }
    }
}
