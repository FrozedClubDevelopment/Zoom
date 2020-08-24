package me.ryzeon.core.manager.chat;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.config.ConfigCursor;

@Getter
@Setter
public class ChatManager {

    private boolean mute = false;
    private int delay = 5;

    public void load() {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat");
        this.mute = configCursor.getBoolean("mute");
        this.delay = configCursor.getInt("delay");
    }

    public void save() {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getSettingsconfig(), "chat");
        configCursor.set("mute", mute);
        configCursor.set("delay", delay);
        configCursor.save();
    }
}
