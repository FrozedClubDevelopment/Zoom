package club.frozed.core.menu.chat.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 6/10/2020 @ 20:08
 */
public class ToggleSoundButton extends Button {

    private PlayerData playerData;

    public ToggleSoundButton(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemCreator(Material.JUKEBOX)
                .setName("§6Toggle Sounds")
                .setLore(Collections.singletonList(this.playerData.isToggleSounds() ? "§aenabled" : "&cdisabled"))
                .get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        playSound(!this.playerData.isToggleSounds(), player);
        this.playerData.setToggleSounds(!this.playerData.isToggleSounds());
    }

    private void playSound(boolean enabled, Player player) {
        if (enabled) {
            playSuccess(player);
        } else {
            playNeutral(player);
        }
    }
}
