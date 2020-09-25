package club.frozed.core.menu.grant.procedure;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.grant.WoolUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Menu;
import club.frozed.core.utils.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 09:36
 */
public class GrantDurationMenu implements Menu {
    private Inventory inventory;
    private PlayerData targetPlayerData;

    private boolean custom;

    private boolean completed;

    public GrantDurationMenu(PlayerData targetPlayerData) {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9 * 3, CC.translate("&aChoose duration."));
        this.targetPlayerData = targetPlayerData;
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();
        ItemCreator fourtyDays = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.GREEN)).setName("&a&l14 Days");
        this.inventory.setItem(10,fourtyDays.get());

        ItemCreator thirtyDays = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.YELLOW)).setName("&6&l30 Days");
        this.inventory.setItem(12,thirtyDays.get());

        ItemCreator perm = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.RED)).setName("&4&lPermanent");
        this.inventory.setItem(14,perm.get());

        ItemCreator customDuration = new ItemCreator(Material.SIGN).setName("&9&lCustom Duration");
        this.inventory.setItem(16,customDuration.get());

        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
        GrantProcedure targetGrantProcedure = data.getGrantProcedure();
        long duration;
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
                case 10:
                    if (targetGrantProcedure == null) {
                        p.closeInventory();
                        p.sendMessage(CC.translate("&4Error! &cOpps"));
                        return;
                    }
                    duration = DateUtils.getDuration("14d");
                    this.completed = true;
                    data.getGrantProcedure().setEnteredDuration(duration);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    p.closeInventory();
                    p.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSound(p,true);
                    break;
                case 12:
                    if (targetGrantProcedure == null) {
                        p.closeInventory();
                        p.sendMessage(CC.translate("&4Error! &cOpps"));
                        return;
                    }
                    duration = DateUtils.getDuration("30d");
                    this.completed = true;
                    data.getGrantProcedure().setEnteredDuration(duration);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    p.closeInventory();
                    p.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSound(p,true);
                    break;
                case 14:
                    if (targetGrantProcedure == null) {
                        p.closeInventory();
                        p.sendMessage(CC.translate("&4Error! &cOpps"));
                        return;
                    }
                    this.completed = true;
                    targetGrantProcedure.setPermanent(true);
                    data.getGrantProcedure().setEnteredDuration(1L);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    p.closeInventory();
                    p.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSound(p,true);
                    break;
                case 16:
                    if (targetGrantProcedure == null) {
                        p.closeInventory();
                        p.sendMessage(CC.translate("&4Error! &cOpps"));
                        return;
                    }
                    this.custom = true;
                    p.closeInventory();
                    p.sendMessage(CC.translate("&aPlease specify a duration, you can use perm or permanent for permanent duration"));
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
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate("&aChoose duration."))){
            PlayerData playerData = PlayerData.getByUuid(event.getPlayer().getUniqueId());
            if (playerData.getGrantProcedure() != null && !this.custom && !this.completed)
                playerData.setGrantProcedure(null);
            if (!targetPlayerData.getPlayer().isOnline()){
                PlayerOfflineData.deleteData(targetPlayerData.getUuid());
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
}
