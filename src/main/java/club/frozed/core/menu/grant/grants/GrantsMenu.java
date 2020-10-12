package club.frozed.core.menu.grant.grants;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.menu.grant.grants.button.GrantInfoButton;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.gui.Button;
import club.frozed.core.utils.gui.buttons.AirButton;
import club.frozed.core.utils.gui.buttons.CloseButton;
import club.frozed.core.utils.gui.buttons.PageInfoButton;
import club.frozed.core.utils.gui.pagination.PageButton;
import club.frozed.core.utils.gui.pagination.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 22:12
 */

public class GrantMenu extends PaginatedMenu {

    private PlayerData targetplayerData;

    private List<Grant> grants = new ArrayList<>();

    public GrantMenu(PlayerData playerData){
        this.targetplayerData = playerData;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(ChatColor.valueOf(targetplayerData.getNameColor()) + targetplayerData.getName() + "'s grants.");
    }

    private Comparator<Grant> GRANT_COMPARATOR = Comparator.comparingLong(Grant::getAddedDate).reversed();

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        targetplayerData.getGrants().stream().sorted(GRANT_COMPARATOR).forEach(grant -> {
            if (grant.isActive() && !grant.hasExpired()){
                grants.add(grant);
            }
        });

        for (int i = 0; i < grants.size(); i++){
            Grant grant = grants.get(i);
            buttons.put(i, new GrantInfoButton(grant, targetplayerData));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new CloseButton()); // FALTA PONR K RETONER AL GRANS MENU AL NORMALITO
        buttons.put(8, new CloseButton());
        buttons.put(36, new CloseButton());
        buttons.put(44, new CloseButton());

        /*
        First Line Glass Button
         */
        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());
        /*
        Second line button
         */
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
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
