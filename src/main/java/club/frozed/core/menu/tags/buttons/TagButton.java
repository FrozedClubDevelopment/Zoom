package club.frozed.core.menu.tags.buttons;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.tags.Tag;
import club.frozed.lib.chat.CC;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
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

    private final Tag tag;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(tag.getTagIcon());
        itemCreator.setName(tag.getTagDisplayName());
        List<String> lore = new ArrayList<>();
        if (player.hasPermission(tag.getTagPermission()) || player.hasPermission("core.tags.all")) {
            for (String msg : tag.getTagLore()) {
                lore.add(CC.translate(msg.replace("<player>", player.getName()).replace("<tag>", tag.getTagPrefix())));
            }
        } else {
            for (String msg : Zoom.getInstance().getTagsConfig().getConfiguration().getStringList("no-perms-lore")) {
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
        if (player.hasPermission(tag.getTagPermission()) || player.hasPermission("core.tags.all")) {
            data.setTag(tag.getTagPrefix());
            playSuccess(player);
        } else {
            player.sendMessage(CC.translate("&cYou don't have this tag"));
            playNeutral(player);
        }

        player.closeInventory();
    }
}
