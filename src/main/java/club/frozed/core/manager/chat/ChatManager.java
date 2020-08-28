package club.frozed.zoom.manager.chat;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.config.ConfigCursor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatManager {

    private boolean mute = false;
    private int delay = 3;

    public void load() {
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getSettingsConfig(), "CHAT");
        this.mute = configCursor.getBoolean("MUTE");
        this.delay = configCursor.getInt("DELAY");
    }

    public void save() {
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getSettingsConfig(), "CHAT");
        configCursor.set("MUTE", mute);
        configCursor.set("DELAY", delay);
        configCursor.save();
    }
}
