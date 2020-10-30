package club.frozed.core.manager.player;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.UUID;

public class PlayerDataLoad implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        Player player = Bukkit.getPlayer(e.getUniqueId());
        if (player != null && player.isOnline()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(CC.translate("&cYou tried to login too quickly after disconnecting.\nTry again in a few seconds."));
            Zoom.getInstance().getServer().getScheduler().runTask(Zoom.getInstance(), () -> player.kickPlayer(CC.translate("&cDuplicate login :/.")));
            return;
        }
        PlayerData playerData = PlayerData.getPlayerData(e.getUniqueId());
        if (playerData == null){
            playerData = PlayerData.createPlayerData(e.getUniqueId(), e.getName());
        }

        if (!playerData.isDataLoaded()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cAn error has ocurred while loading your profile. Please reconnect.");
        }

//        if (playerData.getBannablePunishment() != null) {
//            e.setKickMessage(playerData.getBannablePunishment().toKickMessage(null));
//            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
//            return;
//        }
        if (playerData.getIp() == null){
            playerData.setIp(e.getAddress().getHostAddress());
        }
        if (!playerData.getIpAddresses().contains(e.getAddress().getHostAddress())){
            playerData.getIpAddresses().add(e.getAddress().getHostName());
        }
        if (!playerData.getIp().equalsIgnoreCase(e.getAddress().getHostAddress())){
            playerData.setIp(e.getAddress().getHostAddress());
        }
        playerData.findAlts();

//        for (UUID uuid : playerData.getAlts()){
//            PlayerData altsData = PlayerData.loadData(uuid);
//            if (altsData != null) {
//                if (altsData.getBannablePunishment() != null) {
//                    e.setKickMessage(altsData.getBannablePunishment().toKickMessage(altsData.getName()));
//                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
//                }
//            }
//            PlayerData.deleteData(uuid);
//        }
        playerData.saveData();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cAn error has ocurred while loading your profile. Please reconnect.");
            return;
        }
        playerData.loadPermissions(e.getPlayer());
    }

    private void handledSaveDate(Player player) {
        PlayerData playerData =  PlayerData.getPlayerData(player.getName());
        if (playerData != null){
            playerData.saveData();
            playerData.removeData();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        handledSaveDate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e){
        handledSaveDate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("/gmc") || event.getMessage().equalsIgnoreCase("/gm1")) {
            event.setCancelled(true);
            player.performCommand("gm c");
        } else if (event.getMessage().equalsIgnoreCase("/gms") || event.getMessage().equalsIgnoreCase("/gm0")) {
            event.setCancelled(true);
            player.performCommand("gm s");
        } else if (event.getMessage().equalsIgnoreCase("/gma") || event.getMessage().equalsIgnoreCase("/gm2")) {
            event.setCancelled(true);
            player.performCommand("gm a");
        }
    }
}
