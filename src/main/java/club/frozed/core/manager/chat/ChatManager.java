package club.frozed.core.manager.chat;

import club.frozed.core.Zoom;
import club.frozed.core.utils.config.ConfigCursor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatManager {

    private boolean mute = false;
    private int delay = 3;

    public void load() {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "CHAT");
        this.mute = configCursor.getBoolean("MUTE");
        this.delay = configCursor.getInt("DELAY");
    }

    public void save() {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsConfig(), "CHAT");
        configCursor.set("MUTE", mute);
        configCursor.set("DELAY", delay);
        configCursor.save();
    }
}
