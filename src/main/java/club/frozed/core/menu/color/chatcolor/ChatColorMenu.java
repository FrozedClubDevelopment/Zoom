package club.frozed.core.menu.color.chatcolor;

import club.frozed.core.Zoom;
import club.frozed.core.menu.color.chatcolor.buttons.ChatColorButton;
import club.frozed.core.menu.color.chatcolor.buttons.ResetButton;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.gui.Menu;
import club.frozed.core.utils.gui.buttons.CloseButton;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 10/10/2020 @ 16:13
 */

public class ChatColorMenu extends Menu {
    
    @Override
    public String getTitle(Player player) {
        return CC.translate(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.CHATCOLOR-MENU-TITLE"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new CloseButton());
        buttons.put(8, new CloseButton());

        buttons.put(44, new ResetButton());
        buttons.put(36, new ResetButton());

        buttons.put(11, new ChatColorButton(ChatColor.WHITE, 1));
        buttons.put(12, new ChatColorButton(ChatColor.GOLD, 1));
        buttons.put(13, new ChatColorButton(ChatColor.LIGHT_PURPLE, 1));
        buttons.put(14, new ChatColorButton(ChatColor.AQUA, 1));

        buttons.put(15, new ChatColorButton(ChatColor.YELLOW,2));

        buttons.put(19, new ChatColorButton(ChatColor.DARK_GRAY,1));
        buttons.put(20, new ChatColorButton(ChatColor.GRAY,1));
        buttons.put(21, new ChatColorButton(ChatColor.DARK_AQUA,1));
        buttons.put(22, new ChatColorButton(ChatColor.DARK_PURPLE,1));
        buttons.put(23, new ChatColorButton(ChatColor.DARK_BLUE,1));
        buttons.put(24, new ChatColorButton(ChatColor.DARK_GREEN,1));

        buttons.put(25, new ChatColorButton(ChatColor.RED,3));
        buttons.put(4, new ChatColorButton(ChatColor.DARK_RED,4));

        buttons.put(31, new ChatColorButton(ChatColor.BLACK,1));

        setPlaceholder(true);
        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
