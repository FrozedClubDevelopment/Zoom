package me.ryzeon.core.menu.tags;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.manager.tags.Tag;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.InventoryUtil;
import me.ryzeon.core.utils.items.ItemCreator;
import me.ryzeon.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TagsMenu implements Menu {

    private int page;
    private final Inventory inventory;

    public TagsMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9 * 5, Color.translate(Zoom.getInstance().getTagsconfig().getConfig().getString("title")));
        this.page = 1;
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
        this.inventory.setItem(0, new ItemCreator(Material.RED_ROSE).setName("&cRemove").get());
        this.inventory.setItem(1, glass);
        this.inventory.setItem(2, glass);
        this.inventory.setItem(3, glass);
        this.inventory.setItem(4, glass);
        this.inventory.setItem(5, glass);
        this.inventory.setItem(6, glass);
        this.inventory.setItem(7, glass);
        this.inventory.setItem(8, new ItemCreator(Material.RED_ROSE).setName("&cRemove").get());
        int slot = 9;
        int index = ((page * 27) - 27);
        while (slot < 36 && Zoom.getInstance().getTagManager().getTags().size() > index) {
            Tag tag = Zoom.getInstance().getTagManager().getTags().get(index);
            ItemCreator itemCreator = new ItemCreator(tag.getItemStack());
            itemCreator.setName(tag.getChatColor() + tag.getName());
            List<String> lore = new ArrayList<>();
            if (player.hasPermission(tag.getPermiso())) {
                for (String msg : tag.getLore()) {
                    lore.add(msg.replace("<player>", player.getName()).replace("<tag>", tag.getPrefix()));
                }
            } else {
                for (String msg : Zoom.getInstance().getTagsconfig().getConfig().getStringList("no-perms-lore")) {
                    lore.add(Color.translate(msg));
                }
            }
            itemCreator.setLore(lore);
            this.inventory.addItem(itemCreator.get());
            index++;
            slot++;
        }
        this.inventory.setItem(36, new ItemCreator(Material.RED_ROSE).setName("&cRemove").get());
        this.inventory.setItem(37, new ItemCreator(Material.CARPET, 14).setName("&ePrevious").get());
        this.inventory.setItem(43, new ItemCreator(Material.CARPET, 13).setName("&eNext").get());
        this.inventory.setItem(44, new ItemCreator(Material.RED_ROSE).setName("&cRemove").get());
        InventoryUtil.fillInventory(this.inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
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
                    if (page == 1) return;
                    page--;
                    p.playSound(p.getLocation(), Sound.CLICK, 2F, 2F);
                    update(p);
                    break;
                case 43:
                    if (page == getTotalPages()) return;
                    page++;
                    p.playSound(p.getLocation(), Sound.CLICK, 2F, 2F);
                    update(p);
                    break;
                case 0:
                case 8:
                case 36:
                case 44:
                    if (data.getTag() != null) {
                        data.setTag(null);
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have a prefix");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                default:
                    if (p.hasPermission(Zoom.getInstance().getTagManager().getTagByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())).getPermiso())) {
                        data.setTag(Zoom.getInstance().getTagManager().getTagByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())).getPrefix());
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have this prefix");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
            }
        } else if ((!topInventory.equals(clickedInventory) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || e.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            e.setCancelled(true);
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