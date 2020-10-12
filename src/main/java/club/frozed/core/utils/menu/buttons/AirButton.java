package club.frozed.core.utils.menu.buttons;

import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 20:19
 */

public class AirButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.AIR).get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
