package club.frozed.core.menu.punishments.menus;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.menu.punishments.button.PlayerAltsButton;
import club.frozed.core.menu.punishments.button.PlayerInfoButton;
import club.frozed.core.menu.punishments.button.PunishmentTypeButton;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 13:25
 */
@AllArgsConstructor
public class PunishmentsMenu extends Menu {

    private PlayerData targetData;

    @Override
    public String getTitle(Player player) {
        return ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + "'s Punishments";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(10, new PlayerInfoButton(this.targetData));
        buttonMap.put(12, new PunishmentTypeButton(targetData, PunishmentType.BLACKLIST));
        buttonMap.put(14, new PunishmentTypeButton(targetData, PunishmentType.BAN));
        buttonMap.put(28, new PlayerAltsButton(targetData));
        buttonMap.put(30, new PunishmentTypeButton(targetData, PunishmentType.MUTE));
        buttonMap.put(32, new PunishmentTypeButton(targetData, PunishmentType.WARN));
        buttonMap.put(34, new PunishmentTypeButton(targetData, PunishmentType.KICK));

        return buttonMap;
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
