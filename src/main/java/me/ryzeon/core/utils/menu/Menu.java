package me.ryzeon.core.utils.menu;

import me.ryzeon.core.utils.InventoryUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public interface Menu extends InventoryHolder {
    default void open(Player player) {
        player.openInventory(getInventory());
    }

    void onInventoryClick(InventoryClickEvent paramInventoryClickEvent);

    default void onInventoryDrag(InventoryDragEvent event) {
        if (InventoryUtil.clickedTopInventory(event)) event.setCancelled(true);
    }

    default void onInventoryClose(InventoryCloseEvent event) {

    }
}
