package club.frozed.core.menu.tags.buttons;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.tags.Tag;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 20:44
 */

@AllArgsConstructor
public class TagButton extends Button {

    private Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(tag.getTagIcon());
        itemCreator.setName(tag.getTagDisplayName());
        List<String> lore = new ArrayList<>();
        if (player.hasPermission(tag.getTagPermission())) {
            for (String msg : tag.getTagLore()) {
                lore.add(CC.translate(msg.replace("<player>", player.getName()).replace("<tag>", tag.getTagPrefix())));
            }
        } else {
            for (String msg : Zoom.getInstance().getTagsConfig().getConfig().getStringList("no-perms-lore")) {
                lore.add(CC.translate(msg.replace("<player>", player.getName()).replace("<tag>", tag.getTagPrefix())));
            }
        }
        itemCreator.setLore(lore);
        itemCreator.setAmount(1);
        return itemCreator.get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
        if (player.hasPermission(tag.getTagPermission())) {
            data.setTag(tag.getTagPrefix());
            playSuccess(player);
        } else {
            player.sendMessage("§cYou don't have this prefix");
            playNeutral(player);
        }
        player.closeInventory();
    }
}
