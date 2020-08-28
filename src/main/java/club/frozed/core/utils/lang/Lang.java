package club.frozed.zoom.utils.lang;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
public class Lang {
    public static String TS = ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("social.teamspeak");

    public static String DISCORD = ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("social.discord");

    public static String TWITTER = ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("social.twitter");

    public static String STORE = ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("social.store");

    public static String SERVER_NAME = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("server-name");

    public static String PREFIX = Color.translate(ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("prefix") + " ");

    public static void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

}
