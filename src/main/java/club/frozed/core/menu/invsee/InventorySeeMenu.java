package club.frozed.core.menu.invsee;

import club.frozed.core.menu.invsee.button.ItemButton;
import club.frozed.core.menu.invsee.button.NoArmorButton;
import club.frozed.core.menu.invsee.button.PlayerInfoButton;
import club.frozed.core.menu.invsee.button.PotionButton;
import club.frozed.core.utils.InventoryUtil;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 17:41
 */

public class InventorySeeMenu extends Menu {

    private Player target;

    private BukkitRunnable runnable;

    public InventorySeeMenu(Player player) {
        this.target = player;
    }

    @Override
    public String getTitle(Player player) {
        return "§e" + this.target.getName() + "'s §aInventory";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ItemStack[] targetContents = InventoryUtil.fixInventoryOrder(this.target.getInventory().getContents());

        for (int i = 0; i < targetContents.length; i++) {
            ItemStack itemStack = targetContents[i];

            if (itemStack != null) {
                if (itemStack.getType() != Material.AIR)
                    buttons.put(i, new ItemButton(itemStack));
            }
        }

        if (this.target.getInventory().getHelmet() == null) {
            buttons.put(36, new NoArmorButton("helmet"));
        } else {
            buttons.put(36, new ItemButton(this.target.getInventory().getHelmet()));
        }

        if (this.target.getInventory().getChestplate() == null) {
            buttons.put(37, new NoArmorButton("chestplate"));
        } else {
            buttons.put(37, new ItemButton(this.target.getInventory().getChestplate()));
        }

        if (this.target.getInventory().getLeggings() == null) {
            buttons.put(38, new NoArmorButton("leggings"));
        } else {
            buttons.put(38, new ItemButton(this.target.getInventory().getLeggings()));
        }

        if (this.target.getInventory().getBoots() == null) {
            buttons.put(39, new NoArmorButton("boots"));
        } else {
            buttons.put(39, new ItemButton(this.target.getInventory().getBoots()));
        }

        if (!this.target.getActivePotionEffects().isEmpty()) {
            buttons.put(42, new PotionButton(this.target));
        }

        buttons.put(43, new PlayerInfoButton(this.target, PlayerInfoButton.Type.HEALTH));
        buttons.put(44, new PlayerInfoButton(this.target, PlayerInfoButton.Type.FOOD));

        setPlaceholder(true);
        setAutoUpdate(true);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
