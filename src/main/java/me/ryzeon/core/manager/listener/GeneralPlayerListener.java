package me.ryzeon.core.manager.listener;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.lang.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.ArrayList;
import java.util.List;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        List<String> msg = new ArrayList<>();
        for (String string : Zoom.getInstance().getMessagesconfig().getConfig().getStringList("player.join")) {
            msg.add(translate(string, e.getPlayer()));
        }
        String sound = Zoom.getInstance().getMessagesconfig().getConfig().getString("player.sound");
        if (sound != null || sound.equalsIgnoreCase("none")) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(sound), 2F, 2F);
        }
        e.getPlayer().sendMessage(StringUtils.join(msg, "\n"));
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerKickEvent e) {
        e.setLeaveMessage(null);
    }

    public String translate(String text, Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        text = Color.translate(text);
        text = text
                .replace("<player>", player.getName())
                .replace("<server>", Lang.SERVER_NAME)
//                .replace("<rank>",el rank pero todavia hay xd)
                .replace("<prefix>", playerData.getTag())
                .replace("<teamspeak>", Lang.TS)
                .replace("<store>", Lang.STORE)
                .replace("<twitter>", Lang.TWITTER)
                .replace("<discord>", Lang.DISCORD);
        return text;
    }
}
