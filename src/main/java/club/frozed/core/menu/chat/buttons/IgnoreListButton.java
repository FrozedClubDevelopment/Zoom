package club.frozed.core.menu.chat.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.chat.CC;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 6/10/2020 @ 20:51
 */
public class IgnoreListButton extends Button {

    private PlayerData playerData;

    public IgnoreListButton(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.PAPER)
                .setName("&6Ignore List")
                .setLore(getIgnoreList(this.playerData))
                .get();
    }

    private List<String> getIgnoreList(PlayerData playerData) {
        List<String> ignore = new ArrayList<>();
        ignore.add(CC.MENU_BAR);
        if (playerData.getIgnoredPlayersList().isEmpty()) {
            ignore.add("&cYou aren't ignoring anyone.");
        } else {
            playerData.getIgnoredPlayersList().forEach(name -> ignore.add("&7 » &f" + name));
        }
        ignore.add(CC.MENU_BAR);

        return ignore;
    }
}
