package club.frozed.core.menu.chat;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagesSettingsMenu implements Menu {
    private Inventory inventory;

    public MessagesSettingsMenu(ChatColor chatColor) {
        this.inventory = Bukkit.createInventory((InventoryHolder) this, 9, chatColor + "Chat Settings");
    }

    @Override
    public void open(Player player) {
        this.update(player);
        player.openInventory(this.inventory);
    }

    public void update(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        this.inventory.clear();
        InventoryUtil.fillInventory(this.inventory);
        this.inventory.setItem(0, new ItemCreator(Material.JUKEBOX).setName("§eToggle Sounds").setLore(Arrays.asList(data.isToggleSounds() ? "§aenabled" : "&cdisabled")).get());
        this.inventory.setItem(8, new ItemCreator(Material.SKULL_ITEM).setName("§eToggle Private Messages").setLore(Arrays.asList(data.isTogglePrivateMessages() ? "§aenabled" : "&cdisabled")).get());
        List<String> ignore = new ArrayList<>();
        ignore.add(Color.MENU_BAR);
        if (data.getIgnoredPlayersList().isEmpty()) {
            ignore.add("§cNo players found.");
        } else {
            data.getIgnoredPlayersList().forEach(name -> ignore.add("§7» §f" + name));
        }
        ignore.add(Color.MENU_BAR);
        this.inventory.setItem(4, new ItemCreator(Material.PAPER).setName("§eIgnore List").setLore(ignore).get());
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
                    playSound(p, !data.isToggleSounds());
                    data.setToggleSounds(!data.isToggleSounds());
                    update(p);
                    break;
                case 8:
                    playSound(p, !data.isTogglePrivateMessages());
                    data.setTogglePrivateMessages(!data.isTogglePrivateMessages());
                    update(p);
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
