package me.ryzeon.core.menu.color;

import me.ryzeon.core.utils.menu.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class NameColorMenu implements Menu {

    private final Inventory inventory;

    public NameColorMenu(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent paramInventoryClickEvent) {

    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
