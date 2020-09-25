package club.frozed.core.menu.grant.procedure;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Menu;
import club.frozed.core.utils.time.DateUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
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
    private PlayerData playerData;

    private boolean custom;

    private boolean completed;

    public GrantDurationMenu(PlayerData playerData) {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9 * 4, CC.translate("&aChoose duration."));
        this.playerData = playerData;
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();
        ItemCreator perm = new ItemCreator(Material.DIAMOND).setName("&4&lPermanent");
        this.inventory.setItem(1,perm.get());
        ItemCreator sixtyDays = new ItemCreator(Material.IRON_INGOT).setName("&c&l60 Days");
        this.inventory.setItem(3,sixtyDays.get());
        ItemCreator thirtyDays = new ItemCreator(Material.BEACON).setName("&6&l30 Days");
        this.inventory.setItem(5,thirtyDays.get());
        ItemCreator customDuration = new ItemCreator(Material.BEACON).setName("&e&lCustom Duration");
        this.inventory.setItem(7,customDuration.get());
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
                case 1:
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
                case 3:
                    if (targetGrantProcedure == null) {
                        p.closeInventory();
                        p.sendMessage(CC.translate("&4Error! &cOpps"));
                        return;
                    }
                    duration = DateUtils.getDuration("60d");
                    this.completed = true;
                    data.getGrantProcedure().setEnteredDuration(duration);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    p.closeInventory();
                    p.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSound(p,true);
                    break;
                case 5:
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
                case 7:
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
            if (!playerData.getPlayer().isOnline()){
                PlayerOfflineData.deleteData(playerData.getUuid());
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
