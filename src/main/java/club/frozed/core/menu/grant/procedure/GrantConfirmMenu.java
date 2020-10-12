package club.frozed.core.menu.grant.procedure;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.PlayerOfflineData;
import club.frozed.core.menu.grant.procedure.button.ConfirmCancelButton;
import club.frozed.core.menu.grant.procedure.button.GrantInfoButton;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.menu.Button;
import club.frozed.core.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 23:32
 */

public class GrantConfirmMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return CC.translate("&aConfirm grant?");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerData data = PlayerData.getByUuid(player.getUniqueId());
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ConfirmCancelButton(ConfirmCancelButton.Type.CANCEL, data));

        buttons.put(13, new GrantInfoButton(data));

        buttons.put(15, new ConfirmCancelButton(ConfirmCancelButton.Type.CONFIRM, data));

        setPlaceholder(true);

        return buttons;
    }

    @Override
    public void onClose(Player player) {
        PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());
        if (playerData.getGrantProcedure() != null){
            PlayerOfflineData.deleteData(playerData.getGrantProcedure().getPlayerData().getUuid());
        }
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
