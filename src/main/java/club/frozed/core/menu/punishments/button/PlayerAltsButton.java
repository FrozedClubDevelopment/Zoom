package club.frozed.core.menu.punishments.button;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.lib.chat.CC;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 14:17
 */

@AllArgsConstructor
public class PlayerAltsButton extends Button {

    private PlayerData targetData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.BEACON);
        itemCreator.setName(ChatColor.valueOf(this.targetData.getNameColor()) + "Alts");
        itemCreator.setLore(altsLore());
        return itemCreator.get();
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    private List<String> altsLore() {
        List<String> strings = new ArrayList<>();
        if (this.targetData.getAlts().isEmpty()) {
            strings.add(CC.translate("&c" + this.targetData.getName() + " doesn't have alts."));
            return strings;
        }

        this.targetData.getAlts().forEach(alts -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(alts);
            PlayerData altsData;
            if (offlinePlayer.isOnline()) {
                altsData = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            } else {
                altsData = PlayerData.loadData(offlinePlayer.getUniqueId());
            }
            if (altsData != null) {
                strings.add(CC.translate(Zoom.getInstance().getPunishmentConfig().getConfiguration().getString("MENU.ALTS-INFO")
                        .replace("<player>", altsData.getName() == null ? "None" : altsData.getName())
                        .replace("<status>", getStatusPunishment(altsData))
                ));
            }
        });

        return strings;
    }

    private String getStatusPunishment(PlayerData playerData) {
        String text;

        if (playerData.getActivePunishment(PunishmentType.BLACKLIST) != null) {
            text = playerData.isOnline() ? CC.translate("&8(&4Blacklist&8) + &8(&aOnline&8)") : CC.translate("&8(&4Blacklist&8) + &8(&cOffline&8)");
        } else if (playerData.getActivePunishment(PunishmentType.BAN) != null) {
            text = playerData.isOnline() ? CC.translate("&8(&cBan&8) + &8(&aOnline&8)") : CC.translate("&8(&cBan&8) + &8(&cOffline&8)");
        } else {
            text = playerData.isOnline() ? CC.translate("&8(&aOnline&8)") : CC.translate("&8(&cOffline&8)");
        }

        return text;
    }
}
