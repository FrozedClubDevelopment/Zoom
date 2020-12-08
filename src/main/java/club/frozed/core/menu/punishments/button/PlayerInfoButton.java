package club.frozed.core.menu.punishments.button;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import com.mysql.jdbc.Util;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 14:05
 */

@AllArgsConstructor
public class PlayerInfoButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.SKULL_ITEM, 3);
        itemCreator.setName(ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + "'s Info");
        itemCreator.setOwner(targetData.getName());
        itemCreator.setLore(getItemLore());

        return itemCreator.get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    private List<String> getItemLore() {
        List<String> strings = new ArrayList<>();
        Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("MENU.PLAYER-INFO").forEach(text -> strings.add(CC.translate(text)
                .replace("<player>", this.targetData.getName())
                .replace("<alts>", String.valueOf(this.targetData.getAlts().size()))
                .replace("<country>", getCountry())
        ));

        return strings;
    }

    private String getCountry() {
        String ip = this.targetData.getPlayer().getAddress().toString().replaceAll("/", "");
        String country;
        try {
            country = Utils.getCountry(ip);
        } catch (Exception e) {
            country = null;
        }
        return (country == null ? "Not found" : country);
    }
}
