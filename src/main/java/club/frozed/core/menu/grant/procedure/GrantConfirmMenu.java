package club.frozed.core.menu.grant.procedure;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.TaskUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 19:23
 */

public class GrantConfirmMenu implements Menu {

    private Inventory inventory;

    public GrantConfirmMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9 * 3, CC.translate("&aConfirm grant?"));
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();

        this.inventory.setItem(11, new ItemCreator(Material.INK_SACK, 1).setName("&cCancel Grant").get());

        this.inventory.setItem(13, getInfoGrantItem(player));

        this.inventory.setItem(15, new ItemCreator(Material.INK_SACK, 2).setName("&aConfirm Grant").get());
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerData playerData = PlayerData.getByUuid(p.getUniqueId());
        final Inventory clickedInventory = e.getClickedInventory();
        final Inventory topInventory = e.getView().getTopInventory();
        if (!topInventory.equals(this.inventory)) {
            return;
        }
        if (topInventory.equals(clickedInventory)) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR) || e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))
                return;
            if (!e.getCurrentItem().hasItemMeta()) return;
            int slots = e.getSlot();
            switch (slots) {
                case 11:
                    p.closeInventory();
                    playSound(p,false);
                    break;
                case 16:
                    Rank rankData = Rank.getRankByName(playerData.getGrantProcedure().getRankName());
                    if (rankData == null) {
                        p.closeInventory();
                        p.sendMessage("&4Error! &cThat rank don't exists.");
                        return;
                    }
                    Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
                    grant.setRank(rankData.getName());
                    grant.setActive(true);
                    grant.setServer(playerData.getGrantProcedure().getServer());
                    grant.setAddedDate(System.currentTimeMillis());
                    grant.setAddedBy(p.getName());
                    grant.setDuration(playerData.getGrantProcedure().getEnteredDuration());
                    grant.setPermanent(playerData.getGrantProcedure().isPermanent());
                    grant.setReason(playerData.getGrantProcedure().getEnteredReason());
                    p.closeInventory();
                    TaskUtil.runAsync(() ->{
                        PlayerData targetData = PlayerData.getByUuid(playerData.getGrantProcedure().getPlayerData().getUuid());
                        if (targetData == null) {
                            targetData = PlayerOfflineData.loadData(playerData.getGrantProcedure().getPlayerData().getName());
                        }
                        if (targetData == null) return;
                        targetData.getGrants().add(grant);
                        playSound(p, true);
                        if (grant.isPermanent()) {
                            p.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName()));
                            String json = new RedisMessage(Payload.GRANT_ADD)
                                    .setParam("NAME",targetData.getName())
                                    .setParam("MESSAGE",CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.PERM").replace("<rank>",rankData.getName()))).toJSON();
                            if (Zoom.getInstance().getRedisManager().isActive()){
                                Zoom.getInstance().getRedisManager().write(json);
                            } else {
                                if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                                    targetData.getPlayer().sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.PERM").replace("<rank>",rankData.getName())));
                                }
                            }
                        } else {
                            p.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added permanently grant " + grant.getRank().getName() + " to " + targetData.getName()  + " for " + grant.getNiceDuration()));
                            String json = new RedisMessage(Payload.GRANT_ADD)
                                    .setParam("NAME",targetData.getName())
                                    .setParam("MESSAGE",CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.TEMP")
                                            .replace("<time>",grant.getNiceDuration())
                                            .replace("<rank>",rankData.getName()))).toJSON();
                            if (Zoom.getInstance().getRedisManager().isActive()){
                                Zoom.getInstance().getRedisManager().write(json);
                            } else {
                                if (targetData.getPlayer() != null && targetData.getPlayer().isOnline()) {
                                    targetData.getPlayer().sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.GRANT.TEMP")
                                            .replace("<time>",grant.getNiceDuration())
                                            .replace("<rank>",rankData.getName())));
                                }
                            }
                        }
                        Player target = Bukkit.getPlayer(targetData.getName());
                        if (target == null) {
                            String json = new RedisMessage(Payload.GRANT_UPDATE)
                                    .setParam("NAME",targetData.getName())
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
                            if (Zoom.getInstance().getRedisManager().isActive()){
                                Zoom.getInstance().getRedisManager().write(json);
                            }
                        } else {
                            PlayerData targetPlayerData = PlayerData.getByUuid(target.getUniqueId());
                            targetPlayerData.loadPermissions(target);
                        }
                        PlayerOfflineData.deleteData(targetData.getUuid());
                    });
                    break;
                default:
                    break;
            }
        } else if ((!topInventory.equals(clickedInventory) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate("&aConfirm grant?"))){
            PlayerData playerData = PlayerData.getByUuid(event.getPlayer().getUniqueId());
            if (playerData.getGrantProcedure() != null){
                PlayerOfflineData.deleteData(playerData.getGrantProcedure().getPlayerData().getUuid());
            }
        }
    }

    public void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public ItemStack getInfoGrantItem(Player player){
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        Rank rank = Rank.getRankByName(playerData.getGrantProcedure().getRankName());
        if (rank == null) return new ItemStack(Material.SKULL_ITEM);
        ItemCreator itemStack = new ItemCreator(Material.PAPER);
        itemStack.setName(rank.getColor() + "Grant information");
        List<String> lines = new ArrayList<>();
        lines.add(CC.MENU_BAR);
        lines.add(CC.translate(rank.getColor() + "Rank &7» &f" + playerData.getGrantProcedure().getRankName()));
        lines.add(CC.translate(rank.getColor() + "Player &7» &f" + playerData.getGrantProcedure().getPlayerData().getName()));
        lines.add(CC.translate(rank.getColor() + "Current Player Rank &7» &f" + playerData.getGrantProcedure().getPlayerData().getHighestRank().getColor() + playerData.getGrantProcedure().getPlayerData().getHighestRank().getName()));
        lines.add(CC.translate(rank.getColor() + "Duration &7» &f" + (playerData.getGrantProcedure().isPermanent() ? "Permanent" : playerData.getGrantProcedure().getNiceDuration())));
        lines.add(CC.translate(rank.getColor() + "Reason &7» &f" + playerData.getGrantProcedure().getEnteredReason()));
        lines.add(CC.MENU_BAR);
        itemStack.setLore(lines);

        return itemStack.get();
    }
}
