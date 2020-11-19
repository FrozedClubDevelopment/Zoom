package club.frozed.core.utils.grant;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 09:05
 */

public class WoolUtil {
    public static final ArrayList<ChatColor> woolColors = new ArrayList<>(Arrays.asList(
            ChatColor.WHITE,
            ChatColor.GOLD,
            ChatColor.LIGHT_PURPLE,
            ChatColor.AQUA,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE,
            ChatColor.DARK_GRAY,
            ChatColor.GRAY,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_PURPLE,
            ChatColor.BLUE,
            ChatColor.RESET,
            ChatColor.DARK_GREEN, 
            ChatColor.RED,
            ChatColor.BLACK));

    public static int convertChatColorToWoolData(ChatColor color) {
        if (color == ChatColor.DARK_RED) color = ChatColor.RED;
        if (color == ChatColor.DARK_BLUE) color = ChatColor.BLUE;
        return woolColors.indexOf(color);
    }
}

