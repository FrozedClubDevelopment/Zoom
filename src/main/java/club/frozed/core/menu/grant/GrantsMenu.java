package club.frozed.core.menu.grant;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.menu.grant.procedure.GrantDurationMenu;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.TaskUtil;
import club.frozed.core.utils.grant.GrantUtil;
import club.frozed.core.utils.grant.WoolUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Menu;
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
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 23/09/2020 @ 12:12
 */

public class GrantsMenu implements Menu {

    private int page;
    private final Inventory inventory;
    private PlayerData targetplayerData;
    private List<Grant> grants = new ArrayList<>();

    public GrantsMenu(PlayerData playerData) {
        this.inventory = Bukkit.createInventory(this, 9 * 5, CC.translate(ChatColor.valueOf(playerData.getNameColor()) + playerData.getName() + "'s grants."));
        this.page = 1;
        this.targetplayerData = playerData;
    }

    private int getTotalPages() {
        return Zoom.getInstance().getTagManager().getTags().size() / 28 + 1;
    }

    private Comparator<Grant> GRANT_COMPARATOR = Comparator.comparingLong(Grant::getAddedDate).reversed();

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
        targetplayerData.getGrants().stream().sorted(GRANT_COMPARATOR).forEach(grant -> {
            if (grant.isActive() && !grant.hasExpired()){
                grants.add(grant);
            }
        });
        int slot = 9;
        int index = ((page * 27) - 27);

        while (slot < 36 && grants.size() > index) {
            Grant grant = grants.get(index);
            Rank rank = grant.getRank();
            ItemCreator itemCreator = new ItemCreator(Material.WOOL);
            itemCreator.setName(grant.getRank().getColor() + grant.getRank().getName());
            if (rank != null){
                itemCreator.setName(rank.getColor() + grant.getRank().getName());
            } else {
                itemCreator.setName("&e" + grant.getRankName());
            }
            List<String> lore = new ArrayList<>();
            Zoom.getInstance().getMessagesConfig().getConfig().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.LORE").forEach(text -> {
                switch (text){
                    case "<not-expired>":
                        if (!grant.hasExpired() && grant.getRank() != null && !grant.getRank().isDefaultRank())
                            Zoom.getInstance().getMessagesConfig().getConfig().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.NOT-EXPIRED").forEach(textExpired -> lore.add(CC.translate(textExpired)));
                        break;
                    case "<if-removed>":
                        if (grant.getRemovedBy() != null && !grant.getRemovedBy().equalsIgnoreCase(""))
                            Zoom.getInstance().getMessagesConfig().getConfig().getStringList("COMMANDS.GRANT.GRANT-MENU.GRANTS.IF-REMOVED").forEach(textRemoved -> lore.add(CC.translate(textRemoved)
                                    .replace("<removedBy>",grant.getRemovedBy())
                                    .replace("<removedDate>",GrantUtil.getDate(grant.getRemovedDate()))));
                        break;
                    default:
                        lore.add(translate(grant,text));
                        break;
                }
            });
            itemCreator.setLore(lore);
            itemCreator.setDurability(WoolUtil.convertChatColorToWoolData(grant.getRank().getColor()));
            this.inventory.addItem(itemCreator.get());
            index++;
            slot++;
        }
        this.inventory.setItem(36, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        this.inventory.setItem(37, new ItemCreator(Material.CARPET, 14).setName("&ePrevious").get());
        this.inventory.setItem(40, new ItemCreator(Material.NETHER_STAR, 14).setName("&eAll Grants").get());
        this.inventory.setItem(43, new ItemCreator(Material.CARPET, 13).setName("&eNext").get());
        this.inventory.setItem(44, new ItemCreator(Material.RED_ROSE).setName("&cClose").get());
        InventoryUtil.fillInventory(this.inventory);
    }

    private String translate(Grant grant, String text) {
        text = CC.translate(text);

        text = text
                .replace("<addedBy>",grant.getAddedBy())
                .replace("<addedDate>", GrantUtil.getDate(grant.getAddedDate()))
                .replace("<duration>",grant.getNiceDuration())
                .replace("<reason>",grant.getReason())
                .replace("<active>",!grant.hasExpired() ? "&aYes" : "&cNo")
                .replace("<expire>",grant.getNiceExpire());

        return text;
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
                case 40:
                    new AllGrantsMenu(targetplayerData).open(p);
                    break;
                case 0:
                case 8:
                case 36:
                case 44:
                    p.closeInventory();
                    break;
                default:
                    Grant grant = this.grants.get(e.getSlot() - 9);
                    Rank rank = grant.getRank();
                    if (rank != null && rank.isDefaultRank()) return;
                    if (grant.hasExpired()) return;

                    grant.setActive(false);
                    grant.setRemovedDate(System.currentTimeMillis());
                    grant.setRemovedBy(p.getName());
                    TaskUtil.runAsync(() ->{
                        Player target = Bukkit.getPlayer(targetplayerData.getName());
                        if (target == null) {
                            String json = new RedisMessage(Payload.GRANT_UPDATE)
                                    .setParam("NAME",targetplayerData.getName())
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
                            targetplayerData.loadPermissions(target);
                        }
                        p.closeInventory();
                    });
                    break;
            }
        } else if ((!topInventory.equals(clickedInventory) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(CC.translate(ChatColor.valueOf(targetplayerData.getNameColor()) + targetplayerData.getName() + "'s grants."))){
            PlayerOfflineData.deleteData(targetplayerData.getUuid());
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