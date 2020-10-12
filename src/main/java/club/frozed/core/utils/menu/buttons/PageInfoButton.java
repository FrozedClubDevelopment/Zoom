package club.frozed.core.utils.gui.buttons;

import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.gui.pagination.PaginatedMenu;
import club.frozed.core.utils.items.ItemCreator;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 20:26
 */

@AllArgsConstructor
public class PageInfoButton extends Button {

    private PaginatedMenu paginatedMenu;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.NETHER_STAR)
                .setName("&ePage Info")
                .setLore(Collections.singletonList("&e" + paginatedMenu.getPage() + "&7/&a" + paginatedMenu.getPages(player)))
                .glow()
                .get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
