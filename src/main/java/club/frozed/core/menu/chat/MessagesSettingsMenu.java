package club.frozed.core.menu.chat;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.menu.chat.buttons.IgnoreListButton;
import club.frozed.core.menu.chat.buttons.TogglePrivateMessagesButton;
import club.frozed.core.menu.chat.buttons.ToggleSoundButton;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.menu.Menu;
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

    private final ChatColor chatColor;

    public MessagesSettingsMenu(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_GRAY + "Chat Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
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
