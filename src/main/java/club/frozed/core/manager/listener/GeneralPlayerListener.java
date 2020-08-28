package club.frozed.zoom.manager.listener;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.lang.Lang;
import club.frozed.zoom.manager.player.PlayerData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.ArrayList;
import java.util.List;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        List<String> msg = new ArrayList<>();
        for (String string : ZoomPlugin.getInstance().getMessagesConfig().getConfig().getStringList("NETWORK.JOIN-MESSAGE")) {
            msg.add(translate(string, e.getPlayer()));
        }
        String sound = ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("NETWORK.JOIN-SOUND");
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

    @EventHandler
    public void onSkullClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;
        if (block.getType() != Material.SKULL) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Skull skull = (Skull) block.getState();
        if (!skull.hasOwner()) return;

        player.sendMessage(Color.translate(Lang.PREFIX + ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SKULL-CLICK-MESSAGE"))
                .replace("<player>", skull.getOwner())
        );
    }
}
