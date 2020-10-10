package club.frozed.core.manager.listener;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.lang.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.ArrayList;
import java.util.List;

public class GeneralPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        List<String> msg = new ArrayList<>();
        for (String string : Zoom.getInstance().getMessagesConfig().getConfig().getStringList("NETWORK.JOIN-MESSAGE")) {
            msg.add(translate(string, e.getPlayer()));
        }

        String sound = Zoom.getInstance().getMessagesConfig().getConfig().getString("NETWORK.JOIN-SOUND");
        if (sound != null || sound.equalsIgnoreCase("none")) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(sound), 2F, 2F);
        }

        e.getPlayer().sendMessage(StringUtils.join(msg, "\n"));
        e.setJoinMessage(null);
        if (!PlayerData.getByUuid(e.getPlayer().getUniqueId()).isVote()) {
            List<String> voteMessage = CC.translate(Zoom.getInstance().getSettingsConfig().getConfig().getStringList("SETTINGS.NAME-MC-CHECK.JOIN-MSG"));
            String voteSound = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.NAME-MC-CHECK.SOUND");
            e.getPlayer().sendMessage(StringUtils.join(voteMessage, "\n"));
            if (voteSound != null || !voteSound.equalsIgnoreCase("none")) {
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.valueOf(voteSound), 2F, 2F);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerKickEvent e) {
        e.setLeaveMessage(null);
    }

    public String translate(String text, Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        Rank rankData = Rank.getRankByName(playerData.getName());
        text = CC.translate(text);

        if (playerData.getTag() != null) {
            text = text
                    .replace("<player>", player.getName())
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<rank>", rankData.getName())
                    .replace("<prefix>", playerData.getTag())
                    .replace("<teamspeak>", Lang.TS)
                    .replace("<store>", Lang.STORE)
                    .replace("<twitter>", Lang.TWITTER)
                    .replace("<discord>", Lang.DISCORD);
        } else {
            text = text
                    .replace("<player>", player.getName())
                    .replace("<server>", Lang.SERVER_NAME)
                    .replace("<rank>", rankData.getName())
                    .replace("<prefix>", "")
                    .replace("<teamspeak>", Lang.TS)
                    .replace("<store>", Lang.STORE)
                    .replace("<twitter>", Lang.TWITTER)
                    .replace("<discord>", Lang.DISCORD);
        }

        return text;
    }

    @EventHandler
    public void onSkullClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null) return;
        if (block.getType() != Material.SKULL_ITEM) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Skull skull = (Skull) block.getState();
        if (!skull.hasOwner()) return;

        player.sendMessage(CC.translate(Lang.PREFIX + Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.SKULL-CLICK-MESSAGE"))
                .replace("<player>", skull.getOwner())
        );
    }

    // Sign Color :)

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSignChangeEvent(SignChangeEvent e) {
        String[] signLines = e.getLines();
        for (int i = 0; i < signLines.length; ++i) {
            e.setLine(i, CC.translate(signLines[i]));
        }
    }
}
