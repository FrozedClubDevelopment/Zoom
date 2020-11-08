package club.frozed.core.menu.grant;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.menu.grant.procedure.GrantConfirmMenu;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.time.DateUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 13:21
 */
public class GrantListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGrantProcedure(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        String message = ChatColor.stripColor(event.getMessage());
        if (playerData == null) {
            return;
        }
        if (playerData.getGrantProcedure() == null || playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.START) {
            return;
        }
        if (playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.DURATION) {
            long duration;
            event.setCancelled(true);
            if (message.equalsIgnoreCase("perm") || message.equalsIgnoreCase("permanent")) {
                playerData.getGrantProcedure().setEnteredDuration(1L);
                playerData.getGrantProcedure().setPermanent(true);
                player.sendMessage(CC.translate("&aSuccess! &7You have been duration to &a" + playerData.getGrantProcedure().getNiceDuration()));
                playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                player.sendMessage(CC.translate("&aPlease type a reason for grant."));
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);

                return;
            }

            duration = DateUtils.getDuration(message);
            if (!(duration > 0)) {
                player.sendMessage(CC.translate("&cThe duration isn't valid."));
                return;
            }

            playerData.getGrantProcedure().setEnteredDuration(duration);
            player.sendMessage(CC.translate("&aSuccess! &7You have been duration to &a" + playerData.getGrantProcedure().getNiceDuration()));
            playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
            player.sendMessage(CC.translate("&aPlease type a reason for grant."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else if (playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.REASON) {
            event.setCancelled(true);
            playerData.getGrantProcedure().setEnteredReason(message);
            playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.CONFIRMATION);
            new GrantConfirmMenu().openMenu(player);
        }
    }
}
