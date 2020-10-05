package club.frozed.core.menu.chat;

import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.gui.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public class ChatSettingsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Chat Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return null;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
