package club.frozed.core.menu.invsee.button;

import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.items.ItemCreator;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 18:02
 */

@AllArgsConstructor
public class PlayerInfoButton extends Button {
    private Player target;
    private Type type;

    @Override
    public ItemStack getButtonItem(Player player) {
        return getItem(this.type);
    }

    private ItemStack getItem(Type type){
        ItemStack itemStack = null;
        switch (type){
            case FOOD:
                itemStack = new ItemCreator(Material.COOKED_BEEF).setName("§eFood§7: §e" + this.target.getFoodLevel()).get();
                break;
            case HEALTH:
                itemStack = new ItemCreator(Material.REDSTONE).setName("§eHealth§7: §e" + Math.ceil(this.target.getHealth() / 2.0D) + " &4&l❤").get();
                break;
        }
        return itemStack;
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    public enum Type{
        FOOD,
        HEALTH
    }
}
