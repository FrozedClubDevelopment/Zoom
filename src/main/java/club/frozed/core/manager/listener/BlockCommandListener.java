package club.frozed.core.manager.listener;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class BlockCommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        List<String> blockedCommand = Zoom.getInstance().getSettingsConfig().getConfiguration().getStringList("SETTINGS.COMMANDS-BLOCKED.LIST");

        blockedCommand.forEach(cmd -> {
            if (e.getMessage().equalsIgnoreCase(cmd)) {
                if (e.getPlayer().hasPermission("core.blocked.bypass")) return;
                if (e.getPlayer().isOp()) return;
                e.setCancelled(true);
                e.getPlayer().sendMessage(CC.translate(Zoom.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.COMMANDS-BLOCKED.MSG")));
            }
        });
    }
}
