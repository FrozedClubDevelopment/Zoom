package club.frozed.core.manager.staff.freeze;

import club.frozed.core.Zoom;
import club.frozed.core.manager.event.freeze.*;
import club.frozed.core.utils.Clickable;
import club.frozed.lib.chat.CC;
import club.frozed.lib.config.ConfigCursor;
import club.frozed.lib.task.TaskUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 10/01/2021 @ 05:27 p. m.
 */

public class FreezeListener implements Listener {

    @Getter private static final List<UUID> freezeList = new ArrayList<>();

    private final ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.FREEZE");

    @EventHandler
    public void onPlayerFreezeEvent(PlayerFreezeEvent event) {
        if (event.isCancelled()) return;
        Player sender = event.getSender();
        Player target = event.getPlayer();
        target.sendMessage(CC.translate(configCursor.getString("FROZE.PLAYER")));
        Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.essentials.freeze")).forEach(player -> player.sendMessage(CC.translate(configCursor.getString("FROZE.STAFF")
                .replace("<sender>", sender.getName())
                .replace("<player>", target.getName())
        )));
        freezeList.add(event.getUniqueId());
        handler(target, true);
        startRunnable(target);
    }

    public static void handler(Player target, boolean enabled) {
        if (enabled) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, 200));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2147483647, 200));
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2147483647, 200));
        } else {
            target.removePotionEffect(PotionEffectType.JUMP);
            target.removePotionEffect(PotionEffectType.SLOW);
            target.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    private void sendMessage(Player player) {
        ConfigCursor freeze = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.FREEZE.FREEZE-MESSAGE");
        if (freeze.getBoolean("SOUND.ENABLED")) {
            player.playSound(player.getLocation(), Sound.valueOf(freeze.getString("SOUND.SOUND")), 2F, 2F);
        }
        freeze.getStringList("MESSAGE").forEach(text -> player.sendMessage(CC.translate(text)));
    }

    @EventHandler
    public void onPlayerUnFreezeEvent(PlayerUnFreezeEvent event) {
        if (event.isCancelled()) return;
        Player sender = event.getSender();
        Player target = event.getPlayer();

        target.sendMessage(CC.translate(configCursor.getString("UNFROZE.PLAYER")));
        Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.essentials.freeze")).forEach(player -> {
            player.sendMessage(CC.translate(configCursor.getString("UNFROZE.STAFF")
                    .replace("<sender>", sender.getName())
                    .replace("<player>", target.getName())
            ));
        });

        removeFreeze(event.getUniqueId());
        handler(target, false);
    }

    @EventHandler
    public void onPlayerLeaveFreezeEvent(PlayerLeaveFreezeEvent event) {
        if (event.isCancelled()) return;
        Clickable clickable = new Clickable();
        clickable.add(CC.translate(configCursor.getString("LEAVE-FREEZE").replace("<player>", event.getPlayer().getName())));
        if (configCursor.getBoolean("LEAVE-FREEZE-CLICKABLE.ENABLED")) {
            clickable.add(
                    CC.translate(configCursor.getString("LEAVE-FREEZE-CLICKABLE.TEXT")),
                    CC.translate(configCursor.getString("LEAVE-FREEZE-CLICKABLE.TEXT-CLICKABLE")),
                    configCursor.getString("LEAVE-FREEZE-CLICKABLE.COMMAND").replace("<player>", event.getPlayer().getName())
            );
        }

        Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.essentials.freeze")).forEach(clickable::sendToPlayer);
        TaskUtil.runLater(() -> {
            Player player = Bukkit.getPlayer(event.getUniqueId());
            if (player == null) {
                removeFreeze(event.getUniqueId());
                FreezeHandlerListener.lastFreeze.add(event.getUniqueId());
            }
        }, (2 * 60) * 20);
    }

    @EventHandler
    public void onPlayerJoinFreezeEvent(PlayerJoinFreezeEvent event) {
        if (event.isCancelled()) return;
        Clickable clickable = new Clickable(configCursor.getString("JOIN-FREEZE").replace("<player>", event.getPlayer().getName()));
        Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.essentials.freeze")).forEach(clickable::sendToPlayer);
        handler(event.getPlayer(), true);
        Player target = event.getPlayer();
        startRunnable(target);
    }

    @EventHandler
    public void onPlayerMoveFreeze(PlayerFreezeMoveEvent event) {
        event.getPlayer().teleport(event.getTo());
    }

    private void removeFreeze(UUID uuid) {
        freezeList.remove(uuid);
    }

    private void startRunnable(Player target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!target.isOnline()) {
                    cancel();
                    return;
                }
                if (!freezeList.contains(target.getUniqueId())) {
                    cancel();
                    return;
                }
                if (target.getPlayer() == null) {
                    cancel();
                    return;
                }
                sendMessage(target);
            }
        }.runTaskTimer(Zoom.getInstance(), 40, 100);
    }
}
