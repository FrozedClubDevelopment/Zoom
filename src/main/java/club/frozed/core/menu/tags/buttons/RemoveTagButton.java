package club.frozed.core.menu.tags.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 20:17
 */

public class RemoveTagButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.RED_ROSE).setName("&cRemove").get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        if (data.getTag() != null) {
            data.setTag(null);
            playSuccess(player);
        } else {
            player.sendMessage("§cYou don't have a prefix");
            playNeutral(player);
        }
        player.closeInventory();
    }
}