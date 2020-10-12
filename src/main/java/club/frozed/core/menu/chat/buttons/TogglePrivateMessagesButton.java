package club.frozed.core.menu.chat.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 6/10/2020 @ 20:09
 */

public class TogglePrivateMessagesButton extends Button {

    private PlayerData playerData;

    public TogglePrivateMessagesButton(PlayerData playerData){
        this.playerData = playerData;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setName("§eToggle Private Messages")
                .setLore(Collections.singletonList(this.playerData.isTogglePrivateMessages() ? "§aenabled" : "&cdisabled"))
                .get()
                ;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playSound(!this.playerData.isTogglePrivateMessages(), player);
        this.playerData.setTogglePrivateMessages(!this.playerData.isTogglePrivateMessages());
    }

    private void playSound(boolean xd , Player player){
        if (xd) {
            playSuccess(player);
        } else {
            playNeutral(player);
        }
    }
}
