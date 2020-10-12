package club.frozed.core.utils.menu.buttons;

import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 10/10/2020 @ 23:13
 */

public class CloseButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.INK_SACK).setDurability(1).setName("&cClose").get();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType, int hb) {
        playNeutral(player);
        player.closeInventory();
    }
}
