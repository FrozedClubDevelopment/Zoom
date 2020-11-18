package club.frozed.core.menu.tags.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
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
        return new ItemCreator(Material.RED_ROSE).setName("&cRemove Tag").get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        if (data.getTag() != null) {
            data.setTag(null);
            playSuccess(player);
        } else {
            player.sendMessage("&cYou don't have a tag");
            playNeutral(player);
        }

        player.closeInventory();
    }
}
