package club.frozed.core.menu.color.chatcolor.buttons;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.items.ItemCreator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 10/10/2020 @ 16:38
 */
@AllArgsConstructor
public class ChatColorButton extends Button {

    private ChatColor color;

    private int material;

    @Override
    public ItemStack getButtonItem(Player player) {
        String name = color + WordUtils.capitalize(color.name().replace("_", " ").toLowerCase());
        return new ItemCreator(getMaterial(material))
                .setName(name)
                .setDurability(getDurability(color))
                .get()
                ;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        String permission = "core.chatcolor." + color.name().replace("_", "").toLowerCase();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (player.hasPermission(permission) || player.hasPermission("core.chatcolor.all")) {
            playerData.setChatColor(color.name());
            playSuccess(player);
        } else {
            player.sendMessage("§cYou don't have permission for this color");
            playFail(player);
        }
        player.closeInventory();
    }

    private Material getMaterial(int i){
        Material material = null;
        switch (i){
            case 1:
                material = Material.INK_SACK;
                break;
            case 2:
                material = Material.GLOWSTONE_DUST;
                break;
            case 3:
                material = Material.RED_ROSE;
                break;
            case 4:
                material = Material.NETHER_STALK;
                break;
        }
        return material;
    }

    private int getDurability(ChatColor chatColor) {
        int durability;
        switch (chatColor){
            case WHITE:
                durability = 15;
                break;
            case GOLD:
                durability = 14;
                break;
            case LIGHT_PURPLE:
                durability = 13;
                break;
            case AQUA:
                durability = 12;
                break;
            case YELLOW:
                durability = 0;
                break;
            case DARK_GRAY:
                durability = 8;
                break;
            case GRAY:
                durability = 7;
                break;
            case DARK_AQUA:
                durability = 6;
                break;
            case DARK_PURPLE:
                durability = 5;
                break;
            case DARK_BLUE:
                durability = 4;
                break;
            case DARK_GREEN:
                durability = 2;
                break;
            case RED:
                durability = 0;
                break;
            case DARK_RED:
                durability = 0;
                break;
            case BLACK:
                durability = 0;
                break;
            default:
                durability = 0;
                break;
        }
        return durability;
    }
}
