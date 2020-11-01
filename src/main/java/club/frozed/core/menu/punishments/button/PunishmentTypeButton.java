package club.frozed.core.menu.punishments.button;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.menu.punishments.PunishmentFilter;
import club.frozed.core.menu.punishments.menus.PunishmentsListMenu;
import club.frozed.core.menu.punishments.menus.PunishmentsMenu;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 13:17
 */

@AllArgsConstructor
public class PunishmentTypeButton extends Button {

    private PlayerData targetData;
    private PunishmentType punishmentType;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.WOOL);
        itemCreator.setName(punishmentType.getChatColor() + punishmentType.getPluralName());
        itemCreator.setDurability(punishmentType.getColor().getWoolData());
        return itemCreator.get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        new PunishmentsListMenu(this.targetData, punishmentType, PunishmentFilter.NONE).openMenu(player);
    }
}
