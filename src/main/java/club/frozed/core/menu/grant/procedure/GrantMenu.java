package club.frozed.core.menu.grant.procedure;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.menu.grant.procedure.button.RankButton;
import club.frozed.lib.chat.CC;
import club.frozed.lib.menu.Button;
import club.frozed.lib.menu.buttons.AirButton;
import club.frozed.lib.menu.buttons.CloseButton;
import club.frozed.lib.menu.buttons.PageInfoButton;
import club.frozed.lib.menu.pagination.normal.PageButton;
import club.frozed.lib.menu.pagination.normal.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 12/10/2020 @ 00:14
 */
@AllArgsConstructor
public class GrantMenu extends PaginatedMenu {

    private PlayerData targetData;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate("&8Grant Menu&6");
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (int i = 0; i < getRanks().size(); i++) {
            Rank rank = getRanks().get(i);
            buttons.put(i, new RankButton(rank, targetData));
        }

        return buttons;
    }

    private List<Rank> getRanks() {
        List<Rank> ranks = new ArrayList<>();
        Rank.getRanks().stream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).forEach(ranks::add);

        return ranks;
    }

    @Override
    public void onClose(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.getGrantProcedure() != null && playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.START) {
            playerData.setGrantProcedure(null);
        }
        if (this.targetData != null) {
            PlayerData.deleteOfflineProfile(targetData);
        }
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new CloseButton()); // FALTA PONR K RETONER AL GRANS MENU AL NORMALITO
        buttons.put(8, new CloseButton());
        buttons.put(36, new CloseButton());
        buttons.put(44, new CloseButton());

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
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
