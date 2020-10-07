package club.frozed.core.menu.chat;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.chat.buttons.IgnoreListButton;
import club.frozed.core.menu.chat.buttons.TogglePrivateMessagesButton;
import club.frozed.core.menu.chat.buttons.ToggleSoundButton;
import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.gui.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 6/10/2020 @ 20:05
 */

public class MessagesSettingsMenu extends Menu {

    private ChatColor color;

    public MessagesSettingsMenu(ChatColor chatColor){
        this.color = chatColor;
    }

    @Override
    public String getTitle(Player player) {
        return color + "Chat Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new ToggleSoundButton(playerData));
        buttons.put(4, new IgnoreListButton(playerData));
        buttons.put(8, new TogglePrivateMessagesButton(playerData));

        setPlaceholder(true);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9;
    }
}
