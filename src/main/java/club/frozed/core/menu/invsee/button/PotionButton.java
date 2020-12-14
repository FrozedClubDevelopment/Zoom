package club.frozed.core.menu.invsee.button;

import club.frozed.core.utils.Utils;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 18:00
 */

@AllArgsConstructor
public class PotionButton extends Button {

    private Player target;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> potions = new ArrayList<>();
        for (PotionEffect effect : target.getActivePotionEffects()) {
            String potionName = WordUtils.capitalize(effect.getType().getName().replace("_", "").toLowerCase());
            potions.add("§a" + potionName + " " + (effect.getAmplifier() + 1) + " §efor " + Utils.timeCalculate(effect.getDuration() / 20));
        }
        return new ItemCreator(Material.POTION).setName("§ePotions Effects").setLore(potions).get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }
}
