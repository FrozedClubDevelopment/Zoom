package club.frozed.core.menu.punishments.button;

import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 13:23
 */
@AllArgsConstructor
public class PunishmentInfoButton extends Button {

    private Punishment punishment;

    @Override
    public ItemStack getButtonItem(Player player) {
        return punishment.toItemStack();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
