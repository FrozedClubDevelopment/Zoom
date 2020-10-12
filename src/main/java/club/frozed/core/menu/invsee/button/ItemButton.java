package club.frozed.core.menu.invsee.button;

import club.frozed.core.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 17:45
 */

@AllArgsConstructor
public class ItemButton extends Button {

    private ItemStack itemStack;

    @Override
    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
