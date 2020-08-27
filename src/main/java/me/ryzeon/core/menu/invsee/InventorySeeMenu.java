package me.ryzeon.core.menu.invsee;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.items.ItemCreator;
import me.ryzeon.core.utils.menu.type.ChestMenu;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InventorySeeMenu extends ChestMenu<Zoom> {

    @Getter
    private Player target;

    private BukkitTask runnable;

    public InventorySeeMenu(Player target) {
        super("§e" + target.getName() + "'s §aInventory", 5);
        this.target = target;
        update();
    }

    private void update() {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Inventory inv = getInventory();
                inv.clear();
                inv.setContents(getTarget().getInventory().getContents());
                List<ItemStack> armorcontens = Arrays.asList(getTarget().getInventory().getArmorContents());
                Collections.reverse(armorcontens);
                int indez = inv.getSize() - 9;
                for (ItemStack item : armorcontens) {
                    if (item == null || item.getType() == Material.AIR) {
                        indez++;
                        continue;
                    }
                    inv.setItem(indez++, item);
                }
                if (!getTarget().getActivePotionEffects().isEmpty()) {
                    List<String> potions = new ArrayList<>();
                    for (PotionEffect effect : getTarget().getActivePotionEffects()) {
                        String potionname = WordUtils.capitalize(effect.getType().getName().replace("_", "").toLowerCase());
                        potions.add("§a" + potionname + " " + (effect.getAmplifier() + 1) + " §efor " + Utils.timeCalculate(effect.getDuration() / 20));
                    }
                    getInventory().setItem(42, new ItemCreator(Material.POTION).setName("§ePotions Effects").setLore(potions).get());
                }
                getInventory().setItem(43, new ItemCreator(Material.REDSTONE).setName("§e" + getTarget().getHealth() / 2 + " §4§l❤").get());
                getInventory().setItem(44, new ItemCreator(Material.COOKED_BEEF).setName("§e" + getTarget().getFoodLevel()).get());
            }
        }.runTaskTimerAsynchronously(Zoom.getInstance(), 0, 20);
    }

    public void onInventoryClose(InventoryCloseEvent e) {
        this.runnable.cancel();
        // to stop runnable
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.equals(this.getInventory()))
            return;
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR || item.getType() == Material.STAINED_GLASS_PANE)
                return;
            if (item.getType() == Material.ARROW)
                event.getWhoClicked().closeInventory();
        } else if ((!topInventory.equals(clickedInventory) && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
            event.setCancelled(true);
        }
    }
}
