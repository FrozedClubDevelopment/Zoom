package club.frozed.core.menu.color;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.items.ItemCreator;
import club.frozed.core.utils.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;

public class NameColorMenu implements Menu {
    private Inventory inventory;

    public NameColorMenu() {
        this.inventory = Bukkit.createInventory(this, 9 * 5, Color.translate(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.NAME-COLOR-MENU-TITLE")));
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        this.inventory.clear();
        this.inventory.setItem(0, new ItemCreator(Material.INK_SACK, 1).setName("&eBack").get());
        this.inventory.setItem(8, new ItemCreator(Material.INK_SACK, 1).setName("&eBack").get());
        this.inventory.setItem(44, new ItemCreator(Material.INK_SACK, 8).setName("&cReset").get());
        ChatColor color;
        if (playerData.getNameColor() != null) {
            color = ChatColor.valueOf(playerData.getNameColor());
        } else {
            color = ChatColor.valueOf(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR"));
        }
        this.inventory.setItem(42, new ItemCreator(Material.IRON_DOOR).setName(color + "&oItalic").setLore(Arrays.asList((PlayerData.getByUuid(player.getUniqueId()).isItalic() ? "&aenabled" : "&cdisabled"))).get());
        this.inventory.setItem(38, new ItemCreator(Material.SIGN).setName(color + "&lBold").setLore(Arrays.asList((PlayerData.getByUuid(player.getUniqueId()).isBold() ? "&aenabled" : "&cdisabled"))).get());
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
        this.inventory.setItem(31, new ItemCreator(Material.INK_SACK, 0).setName("&0Black").get());
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
                    if (data.getNameColor() != null) {
                        data.setNameColor(null);
                        playSound(p, true);
                        update(p);
                        break;
                    } else {
                        p.sendMessage("§cYou don't have any chat color");
                        playSound(p, false);
                        p.closeInventory();
                        break;
                    }
                case 11:
                    if (p.hasPermission("core.nameColor.white") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("WHITE");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 12:
                    if (p.hasPermission("core.nameColor.orange") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("GOLD");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 13:
                    if (p.hasPermission("core.nameColor.lightpurple") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("LIGHT_PURPLE");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 14:
                    if (p.hasPermission("core.nameColor.lightblue") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("AQUA");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 15:
                    if (p.hasPermission("core.nameColor.yellow") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("YELLOW");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 19:
                    if (p.hasPermission("core.nameColor.darkgray") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_GRAY");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 20:
                    if (p.hasPermission("core.nameColor.gray") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("GRAY");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 21:
                    if (p.hasPermission("core.nameColor.cyan") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_AQUA");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 22:
                    if (p.hasPermission("core.nameColor.purple") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_PURPLE");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 23:
                    if (p.hasPermission("core.nameColor.darkblue") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_BLUE");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 24:
                    if (p.hasPermission("core.nameColor.darkgreen") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_GREEN");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 25:
                    if (p.hasPermission("core.nameColor.red") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("RED");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 4:
                    if (p.hasPermission("core.nameColor.darkred") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("DARK_RED");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 31:
                    if (p.hasPermission("core.nameColor.black") || p.hasPermission("core.nameColor.all")) {
                        data.setNameColor("BLACK");
                        playSound(p, true);
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this color");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 42:
                    if (p.hasPermission("core.nameColor.italic")) {
                        playSound(p, !data.isItalic());
                        data.setItalic(!data.isItalic());
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this option");
                        playSound(p, false);
                        p.closeInventory();
                    }
                    break;
                case 38:
                    if (p.hasPermission("core.nameColor.bold")) {
                        playSound(p, !data.isBold());
                        data.setBold(!data.isBold());
                        update(p);
                    } else {
                        p.sendMessage("§cYou don't have permission for this option");
                        playSound(p, false);
                        p.closeInventory();
                    }
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

