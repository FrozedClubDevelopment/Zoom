package club.frozed.core.menu.color.namecolor.buttons;

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
 * Date: 11/10/2020 @ 15:55
 */
public class ResetButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.INK_SACK).setDurability(8).setName("&cReset Name Color").get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        if (data.getNameColor() != null) {
            data.setNameColor(null);
            playSuccess(player);
        } else {
            player.sendMessage("§cYou don't have any name color");
            playNeutral(player);
            player.closeInventory();
        }
    }
}
