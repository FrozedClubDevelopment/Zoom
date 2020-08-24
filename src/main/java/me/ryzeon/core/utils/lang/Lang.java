package me.ryzeon.core.utils.lang;

import lombok.Getter;
import me.ryzeon.core.Zoom;

@Getter
public class Lang {
    public static String TS = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.teamspeak");

    public static String DISCORD = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.discord");

    public static String TWITTER = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.twitter");

    public static String STORE = Zoom.getInstance().getMessagesconfig().getConfig().getString("social.store");

    public static String SERVER_NAME = Zoom.getInstance().getSettingsconfig().getConfig().getString("server-name");

}
