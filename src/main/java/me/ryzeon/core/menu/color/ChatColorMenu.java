package me.ryzeon.core.menu.color;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.InventoryUtil;
import me.ryzeon.core.utils.items.ItemCreator;
import me.ryzeon.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ChatColorMenu implements Menu {
    private Inventory inventory;

    public ChatColorMenu() {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9 * 5, Color.translate(Zoom.getInstance().getSettingsconfig().getConfig().getString("chat.chat-color-menu")));
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        this.inventory.clear();
        this.inventory.setItem(0, new ItemCreator(Material.INK_SACK, 1).setName("&eBack").get());
        this.inventory.setItem(8, new ItemCreator(Material.INK_SACK, 1).setName("&eBack").get());
        this.inventory.setItem(44, new ItemCreator(Material.INK_SACK, 8).setName("&cReset").get());
        this.inventory.setItem(36, new ItemCreator(Material.INK_SACK, 8).setName("&cReset").get());
        this.inventory.setItem(11, new ItemCreator(Material.INK_SACK, 15).setName("&fWhite").get());
        this.inventory.setItem(12, new ItemCreator(Material.INK_SACK, 14).setName("&6Orange").get());
        this.inventory.setItem(13, new ItemCreator(Material.INK_SACK, 13).setName("&dLight Purple").get());
        this.inventory.setItem(14, new ItemCreator(Material.INK_SACK, 12).setName("&bLight Blue").get());
        this.inventory.setItem(15, new ItemCreator(Material.GLOWSTONE_DUST).setName("&eYellow").get());
        this.inventory.setItem(19, new ItemCreator(Material.INK_SACK, 8).setName("&8Dark Gray").get());
        this.inventory.setItem(20, new ItemCreator(Material.INK_SACK, 7).setName("&7Gray").get());
        this.inventory.setItem(21, new ItemCreator(Material.INK_SACK, 6).setName("&9Cyan").get());
        this.inventory.setItem(22, new ItemCreator(Material.INK_SACK, 5).setName("&5Purple").get());
        this.inventory.setItem(23, new ItemCreator(Material.INK_SACK, 4).setName("&1Dark Blue").get());
        this.inventory.setItem(24, new ItemCreator(Material.INK_SACK, 2).setName("&2Dark Green").get());
        this.inventory.setItem(25, new ItemCreator(Material.RED_ROSE).setName("&cRed").get());
        this.inventory.setItem(4, new ItemCreator(Material.NETHER_STALK).setName("&4Dark Red").get());
        this.inventory.setItem(31, new ItemCreator(Material.INK_SACK, 0).setName("&1Black").get());
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
                case 0:
                case 8:
                    p.closeInventory();
                    break;
                case 44:
                case 36:
                    if (data.getChatColor() != null) {
                        data.setChatColor(null);
                        playSound(p, true);
                        break;
                    } else {
                        p.sendMessage("§cYou don't have any chat color");
                        playSound(p, false);
                        p.closeInventory();
                        break;
                    }
                case 11:
                    if (p.hasPermission("core.chatcolor.white") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("WHITE");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 12:
                    if (p.hasPermission("core.chatcolor.orange") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("GOLD");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 13:
                    if (p.hasPermission("core.chatcolor.lightpurple") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("LIGHT_PURPLE");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 14:
                    if (p.hasPermission("core.chatcolor.lightblue") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("AQUA");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 15:
                    if (p.hasPermission("core.chatcolor.yellow") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("YELLOW");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 19:
                    if (p.hasPermission("core.chatcolor.darkgray") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_GRAY");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 20:
                    if (p.hasPermission("core.chatcolor.gray") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("GRAY");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 21:
                    if (p.hasPermission("core.chatcolor.cyan") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_AQUA");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 22:
                    if (p.hasPermission("core.chatcolor.purple") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_PURPLE");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 23:
                    if (p.hasPermission("core.chatcolor.darkblue") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_BLUE");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 24:
                    if (p.hasPermission("core.chatcolor.darkgreen") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_GREEN");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 25:
                    if (p.hasPermission("core.chatcolor.red") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("RED");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 4:
                    if (p.hasPermission("core.chatcolor.darkred") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("DARK_RED");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 31:
                    if (p.hasPermission("core.chatcolor.black") || p.hasPermission("core.chatcolor.all")) {
                        data.setChatColor("BLACK");
                        playSound(p, true);
                        p.closeInventory();
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    ;
                    break;
                default:
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
