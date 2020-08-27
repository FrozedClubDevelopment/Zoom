package me.ryzeon.core.utils.lang;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
public class Lang {
    public static String TS = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.teamspeak");

    public static String DISCORD = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.discord");

    public static String TWITTER = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.twitter");

    public static String STORE = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.store");

    public static String SERVER_NAME = Zoom.getInstance().getSettingsconfig().getConfig().getString("server-name");

    public static String PREFIX = Color.translate(Zoom.getInstance().getSettingsconfig().getConfig().getString("prefix") + " ");

    public static void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

}
