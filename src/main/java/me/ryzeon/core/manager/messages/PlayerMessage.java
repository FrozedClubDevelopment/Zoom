package me.ryzeon.core.manager.messages;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerMessage {

    private Player sender;
    private Player recipient;
    private String message;
    private boolean reply;

    public PlayerMessage(Player sender, Player recipient, String message, boolean reply) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.reply = reply;
    }

    public void send() {
        Zoom.getInstance().getMessageManager().getLastReplied().put(sender.getUniqueId(), recipient.getUniqueId());
        Zoom.getInstance().getMessageManager().getLastReplied().put(recipient.getUniqueId(), sender.getUniqueId());

        String senderformat = Zoom.getInstance().getSettingsconfig().getConfig().getString("messages.format.sender")
                .replace("<recipient>", this.recipient.getName())
                .replace("<text>", this.message);
        String recipientformat = Zoom.getInstance().getSettingsconfig().getConfig().getString("messages.format.recipient")
                .replace("<sender>", this.sender.getName())
                .replace("<text>", this.message);
        String socialspyformat = Zoom.getInstance().getSettingsconfig().getConfig().getString("messages.format.social-spy")
                .replace("<sender>", this.sender.getName())
                .replace("<recipient>", this.recipient.getName())
                .replace("<text>", this.message);

        PlayerData recipientdata = PlayerData.getByUuid(this.recipient.getUniqueId());
        if (recipientdata.isTogglesound()) {
            String sound = Zoom.getInstance().getSettingsconfig().getConfig().getString("messages.sound");
            if (!(sound.equals("none") || sound.equals("NONE") || sound == null)) {
                this.recipient.playSound(this.recipient.getLocation(), Sound.valueOf(sound), 2F, 2F);
            }
        }
        this.sender.sendMessage(Color.translate(senderformat));
        this.recipient.sendMessage(Color.translate(recipientformat));
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerData.getByUuid(p.getUniqueId());
            if (data == null) return;
            if (data.isSocialspy() && p.hasPermission("core.chat.socialspy")) {
                p.sendMessage(Color.translate(socialspyformat));
            }
        }
    }
}
