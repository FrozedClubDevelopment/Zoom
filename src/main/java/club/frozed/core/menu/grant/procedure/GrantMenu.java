package club.frozed.core.menu.grant.procedure;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.grant.WoolUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Menu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 23/09/2020 @ 12:12
 */

@Getter
public class GrantMenu implements Menu {

    private int page;
    private final Inventory inventory;
    private PlayerData targetplayerData;

    public GrantMenu(PlayerData playerData) {
        this.inventory = Bukkit.createInventory(this, 9 * 5, CC.translate("&8Grant Menu"));
        this.page = 1;
        this.targetplayerData = playerData;
    }

    private int getTotalPages() {
        return Zoom.getInstance().getTagManager().getTags().size() / 28 + 1;
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
        InventoryUtil.fillInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();
        ItemStack glass = new ItemCreator(Material.STAINED_GLASS_PANE, 7).setName(" ").get();
        this.inventory.setItem(0, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        this.inventory.setItem(1, glass);
        this.inventory.setItem(2, glass);
        this.inventory.setItem(3, glass);
        this.inventory.setItem(4, glass);
        this.inventory.setItem(5, glass);
        this.inventory.setItem(6, glass);
        this.inventory.setItem(7, glass);
        this.inventory.setItem(8, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        int slot = 9;
        int index = ((page * 27) - 27);

        while (slot < 36 && Rank.getRanks().size() > index) {
            Rank rank = Rank.getRanks().get(index);
            ItemCreator itemCreator = new ItemCreator(Material.WOOL);
            itemCreator.setName(rank.getColor() + rank.getName());
            List<String> lore = new ArrayList<>();
            Zoom.getInstance().getMessagesConfig().getConfig().getStringList("COMMANDS.GRANT.GRANT-MENU.RANK").forEach(line -> lore.add(CC.translate(line)
                    .replace("<rank>",rank.getColor() + rank.getName())
                    .replace("<name>",targetplayerData.getName())));
            itemCreator.setLore(lore);
            itemCreator.setDurability(WoolUtil.convertChatColorToWoolData(rank.getColor()));
            this.inventory.addItem(itemCreator.get());
            index++;
            slot++;
        }
        this.inventory.setItem(36, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        this.inventory.setItem(37, new ItemCreator(Material.CARPET, 14).setName("&ePrevious").get());
        this.inventory.setItem(43, new ItemCreator(Material.CARPET, 13).setName("&eNext").get());
        this.inventory.setItem(44, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerData senderData = PlayerData.getByUuid(p.getUniqueId());
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
                case 37:
                    if (page == 1) {
                        p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
                        return;
                    }
                    page--;
                    p.playSound(p.getLocation(), Sound.CLICK, 2F, 2F);
                    update(p);
                    break;
                case 43:
                    if (page == getTotalPages()) {
                        p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
                        return;
                    }
                    page++;
                    p.playSound(p.getLocation(), Sound.CLICK, 2F, 2F);
                    update(p);
                    break;
                case 0:
                case 8:
                case 36:
                case 44:
                    p.closeInventory();
                    break;
                default:
                    Rank rank = Rank.getRankByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    if (rank != null){
                        if (Zoom.getInstance().getRankManager().getDefaultRank() == rank){
                            p.sendMessage(CC.translate("&4Error! &cYou cannot grant this range as the default."));
                            break;
                        }
                        if (targetplayerData.hasRank(rank)){
                            p.sendMessage(CC.translate("&4Error! &cThat player already has that rank."));
                            break;
                        }
                        if (!senderData.canGrant(targetplayerData,rank) && !senderData.hasPermission("core.rank.grant.all")){
                            p.sendMessage(CC.translate("&4Error! &cYou cannot give yourself that rank as it is higher than yours."));
                            break;
                        }
                        senderData.setGrantProcedure(new GrantProcedure(targetplayerData));
                        senderData.getGrantProcedure().setRankName(rank.getName());
                        senderData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.DURATION);
                        senderData.getGrantProcedure().setServer("Global");
                        new GrantDurationMenu(targetplayerData).open(p);
                        p.closeInventory();
                    }
                    break;
            }
        } else if ((!topInventory.equals(clickedInventory) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate("&8Grant Menu"))){
            PlayerData playerData = PlayerData.getByUuid(event.getPlayer().getUniqueId());
            if (playerData.getGrantProcedure() != null && playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.START)
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
