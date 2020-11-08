package club.frozed.core.menu.grant.procedure.button;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.TaskUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 23:25
 */

@AllArgsConstructor
public class ConfirmCancelButton extends Button {

    private Type type;

    private PlayerData data;

    @Override
    public ItemStack getButtonItem(Player player) {
        return getItem(type);
    }

    private ItemStack getItem(Type type) {
        ItemStack itemStack = null;
        switch (type) {
            case CONFIRM:
                itemStack = new ItemCreator(Material.INK_SACK).setDurability(2).setName("&aConfirm Grant").get();
                break;
            case CANCEL:
                itemStack = new ItemCreator(Material.INK_SACK, 1).setName("&cCancel Grant").get();
                break;
        }
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        switch (type) {
            case CANCEL:
                playNeutral(player);
                player.closeInventory();
                break;
            case CONFIRM:
                Rank rankData = Rank.getRankByName(data.getGrantProcedure().getRankName());
                if (rankData == null) {
                    player.closeInventory();
                    player.sendMessage("&4Error! &cThat rank doesn't exist.");
                    return;
                }
                Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
                grant.setRankName(rankData.getName());
                grant.setActive(true);
                grant.setServer(data.getGrantProcedure().getServer());
                grant.setAddedDate(System.currentTimeMillis());
                grant.setAddedBy(player.getName());
                grant.setDuration(data.getGrantProcedure().getEnteredDuration());
                grant.setPermanent(data.getGrantProcedure().isPermanent());
                grant.setReason(data.getGrantProcedure().getEnteredReason());
                player.closeInventory();
                TaskUtil.runAsync(() -> {
                    PlayerData targetData = PlayerData.getPlayerData(data.getGrantProcedure().getPlayerData().getUuid());
                    if (targetData == null) {
                        targetData = PlayerData.loadData(data.getGrantProcedure().getPlayerData().getUuid());
                    }
                    if (targetData == null) return;
                    targetData.getGrants().add(grant);
                    playSuccess(player);
                    if (grant.isPermanent()) {
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName()));
                        String json = new RedisMessage(Payload.GRANT_ADD)
                                .setParam("NAME", targetData.getName())
                                .setParam("MESSAGE", CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.PERM").replace("<rank>", rankData.getName()))).toJSON();
                        if (Zoom.getInstance().getRedisManager().isActive()) {
                            Zoom.getInstance().getRedisManager().write(json);
                        } else {
                            if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                                targetData.getPlayer().sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.PERM").replace("<rank>", rankData.getName())));
                            }
                        }
                    } else {
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName() + " for " + grant.getNiceDuration()));
                        String json = new RedisMessage(Payload.GRANT_ADD)
                                .setParam("NAME", targetData.getName())
                                .setParam("MESSAGE", CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.TEMP")
                                        .replace("<time>", grant.getNiceDuration())
                                        .replace("<rank>", rankData.getName()))).toJSON();
                        if (Zoom.getInstance().getRedisManager().isActive()) {
                            Zoom.getInstance().getRedisManager().write(json);
                        } else {
                            if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                                targetData.getPlayer().sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.TEMP")
                                        .replace("<time>", grant.getNiceDuration())
                                        .replace("<rank>", rankData.getName())));
                            }
                        }
                    }
                    Player target = Bukkit.getPlayer(targetData.getName());
                    if (target == null) {
                        String json = new RedisMessage(Payload.GRANT_UPDATE)
                                .setParam("NAME", targetData.getName())
                                .setParam("GRANT", grant.getRank().getName()
                                        + ";" + grant.getAddedDate()
                                        + ";" + grant.getDuration()
                                        + ";" + grant.getRemovedDate()
                                        + ";" + grant.getAddedBy()
                                        + ";" + grant.getReason()
                                        + ";" + grant.getRemovedBy()
                                        + ";" + grant.isActive()
                                        + ";" + grant.isPermanent()
                                        + ";" + grant.getServer()).toJSON();
                        if (Zoom.getInstance().getRedisManager().isActive()) {
                            Zoom.getInstance().getRedisManager().write(json);
                        }
                    } else {
                        PlayerData targetPlayerData = PlayerData.getPlayerData(target.getUniqueId());
                        targetPlayerData.loadPermissions(target);
                    }
                    if (targetData.isOnline()) {
                        targetData.saveData();
                    } else {
                        PlayerData.deleteOfflineProfile(targetData);
                    }
                });
                break;
        }
    }

    public enum Type {
        CONFIRM,
        CANCEL
    }
}
