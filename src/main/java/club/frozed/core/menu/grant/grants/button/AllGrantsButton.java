package club.frozed.core.menu.grant.grants.button;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.grant.grants.AllGrantsMenu;
import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.items.ItemCreator;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 22:31
 */
@AllArgsConstructor
public class GrantsButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.NETHER_STAR).setName("&eClick to view all grants").glow().get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new AllGrantsMenu(targetData).openMenu(player);
        playSuccess(player);
    }
}
