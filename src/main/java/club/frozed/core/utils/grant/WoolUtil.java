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
            ChatColor.RED,
            ChatColor.YELLOW,
            ChatColor.GOLD,
            ChatColor.DARK_GREEN,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.DARK_AQUA,
            ChatColor.BLUE,
            ChatColor.LIGHT_PURPLE,
            ChatColor.DARK_PURPLE,
            ChatColor.WHITE,
            ChatColor.GRAY,
            ChatColor.DARK_GRAY,
            ChatColor.BLACK,
            ChatColor.RESET
    ));

    public static int convertChatColorToWoolData(ChatColor color) {
        if (color == ChatColor.DARK_RED) color = ChatColor.RED;
        if (color == ChatColor.DARK_BLUE) color = ChatColor.BLUE;

        return woolColors.indexOf(color);
    }
}
