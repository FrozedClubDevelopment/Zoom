package club.frozed.core.utils.lang;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
public class Lang {
    public static String TS = Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.TEAMSPEAK");
    public static String DISCORD = Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.DISCORD");
    public static String TWITTER = Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.TWITTER");
    public static String STORE = Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SOCIAL.STORE");

    public static String SERVER_NAME = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.SERVER-NAME");
    public static String PREFIX = Color.translate(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.PREFIX") + " ");

    public static void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

}
