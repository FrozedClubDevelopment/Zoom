package club.frozed.core.menu.punishments.menus;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.menu.punishments.PunishmentFilter;
import club.frozed.core.menu.punishments.button.PunishmentInfoButton;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.menu.buttons.AirButton;
import club.frozed.core.utils.menu.buttons.BackButton;
import club.frozed.core.utils.menu.buttons.PageInfoButton;
import club.frozed.core.utils.menu.pagination.PageButton;
import club.frozed.core.utils.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 1/11/2020 @ 14:32
 */

@AllArgsConstructor
public class PunishmentsListMenu extends PaginatedMenu {

    {
        setUpdateAfterClick(true);
    }

    private PlayerData targetData;
    private PunishmentType punishmentType;
    private PunishmentFilter punishmentFilter;

    public static boolean closeBack;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return ChatColor.valueOf(targetData.getNameColor()) + targetData.getName() + " " + punishmentType.getPluralName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (Punishment punishment : this.targetData.getPunishmentsByFilter(punishmentType, punishmentFilter)) {
            buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(8, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(36, new BackButton(new PunishmentsMenu(this.targetData)));
        buttons.put(44, new BackButton(new PunishmentsMenu(this.targetData)));

        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());
        buttons.put(42, new AirButton());
        buttons.put(41, new AirButton());
        buttons.put(39, new AirButton());
        buttons.put(38, new AirButton());

        // Pages Button
        buttons.put(43, new PageButton(1, this));
        buttons.put(37, new PageButton(-1, this));

        // Page Info Button
        buttons.put(40, new PageInfoButton(this));

        return buttons;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }
}

