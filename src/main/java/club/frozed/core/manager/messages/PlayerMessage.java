package club.frozed.zoom.manager.messages;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import lombok.Getter;
import lombok.Setter;
import club.frozed.zoom.manager.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter @Setter
public class PlayerMessage {

    private Player sender;
    private Player target;
    private String message;
    private boolean reply;

    public PlayerMessage(Player sender, Player target, String message, boolean reply) {
        this.sender = sender;
        this.target = target;
        this.message = message;
        this.reply = reply;
    }

    public void send() {
        ZoomPlugin.getInstance().getMessageManager().getLastReplied().put(sender.getUniqueId(), target.getUniqueId());
        ZoomPlugin.getInstance().getMessageManager().getLastReplied().put(target.getUniqueId(), sender.getUniqueId());

        String senderFormat = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("PRIVATE-MESSAGES.FORMAT.SENDER")
                .replace("<target>", this.target.getName())
                .replace("<text>", this.message);
        String targetFormat = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("PRIVATE-MESSAGES.FORMAT.TARGET")
                .replace("<sender>", this.sender.getName())
                .replace("<text>", this.message);
        String socialSpyFormat = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("PRIVATE-MESSAGES.FORMAT.SOCIAL-SPY")
                .replace("<sender>", this.sender.getName())
                .replace("<target>", this.target.getName())
                .replace("<text>", this.message);

        PlayerData targetData = PlayerData.getByUuid(this.target.getUniqueId());
        if (targetData.isToggleSounds()) {
            String sound = ZoomPlugin.getInstance().getSettingsConfig().getConfig().getString("PRIVATE-MESSAGES.NOTIFICATION-SOUND");
            if (!(sound.equals("none") || sound.equals("NONE") || sound == null)) {
                this.target.playSound(this.target.getLocation(), Sound.valueOf(sound), 2F, 2F);
            }
        }

        this.sender.sendMessage(Color.translate(senderFormat));
        this.target.sendMessage(Color.translate(targetFormat));

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerData.getByUuid(p.getUniqueId());
            if (data == null) return;
            if (data.isSocialSpy() && p.hasPermission("core.chat.socialSpy")) {
                p.sendMessage(Color.translate(socialSpyFormat));
            }
        }
    }
}
