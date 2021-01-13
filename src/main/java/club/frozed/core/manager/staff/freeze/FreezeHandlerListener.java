package club.frozed.core.manager.staff.freeze;

import club.frozed.core.Zoom;
import club.frozed.core.manager.event.freeze.PlayerFreezeMoveEvent;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/01/2021 @ 10:18 p. m.
 */

public class FreezeHandlerListener implements Listener {

    public static List<UUID> lastFreeze = new ArrayList<>();

    private final ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.FREEZE");

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (lastFreeze.contains(event.getPlayer().getUniqueId())) {
            FreezeListener.handler(event.getPlayer(), false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFreezeMove(PlayerMoveEvent event) {
        PlayerData data = PlayerData.getPlayerData(event.getPlayer());
        if (!data.isFreeze()) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if ((from.getX() != to.getX()) || (from.getZ() != to.getZ())) {
            new PlayerFreezeMoveEvent(event.getPlayer(), from, to).call();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.isFreeze(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(this.isFreeze(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleInventoryClick(InventoryClickEvent event) {
        event.setCancelled(this.isFreeze((Player) event.getWhoClicked()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleInventoryClick(InventoryCreativeEvent event) {
        event.setCancelled(this.isFreeze((Player) event.getWhoClicked()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player)) return;
        event.setCancelled(this.isFreeze((Player) event.getEntered()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleEnter(PlayerCommandPreprocessEvent event) {
        event.setCancelled(this.isFreeze(event.getPlayer()) && !canMakeCommand(event.getMessage()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(this.isFreeze((Player) event.getEntity()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void handleEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        event.setCancelled(this.isFreeze((Player) event.getDamager()));
    }

    private boolean isFreeze(Player player) {
        return FreezeListener.getFreezeList().contains(player.getUniqueId());
    }

    private boolean canMakeCommand(String text) {
        for (String s : configCursor.getStringList("FREEZE-ALLOWED-COMMANDS")) {
            if (text.toLowerCase().startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
