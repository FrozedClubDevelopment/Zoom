package club.frozed.core.manager.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataLoad implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        PlayerData playerData = PlayerData.getByName(e.getName());

        if (playerData == null) {
            playerData = new PlayerData(e.getName(), e.getUniqueId());
        }
        if (!playerData.isDataLoaded()) {
            playerData.loadData();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent e) {
        PlayerData playerData = PlayerData.getByUuid(e.getPlayer().getUniqueId());
        if (playerData == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cAn error has ocurred while loading your profile. Please reconnect.");
            return;
        }
        playerData.loadPermissions(e.getPlayer());
    }

    private void handledSaveDate(Player player) {
        PlayerData playerData = PlayerData.getByName(player.getName());
        playerData.saveData();
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        handledSaveDate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e){
        handledSaveDate(e.getPlayer());
    }
}
