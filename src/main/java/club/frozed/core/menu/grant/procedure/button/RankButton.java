package club.frozed.core.menu.grant.procedure.button;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.menu.grant.procedure.GrantDurationMenu;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.grant.WoolUtil;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 12/10/2020 @ 00:19
 */
@AllArgsConstructor
public class RankButton extends Button {

    private Rank rank;
    private PlayerData targetPlayerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemCreator itemCreator = new ItemCreator(Material.WOOL);
        itemCreator.setName(rank.getColor() + rank.getName());
        List<String> lore = new ArrayList<>();
        Zoom.getInstance().getMessagesConfig().getConfiguration().getStringList("COMMANDS.GRANT.GRANT-MENU.RANK").forEach(line -> lore.add(CC.translate(line)
                .replace("<rank>", rank.getColor() + rank.getName())
                .replace("<name>", targetPlayerData.getName())));
        itemCreator.setLore(lore);
        itemCreator.setDurability(WoolUtil.convertChatColorToWoolData(rank.getColor()));

        return itemCreator.get();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
        PlayerData senderData = PlayerData.getPlayerData(player.getUniqueId());
        if (Zoom.getInstance().getRankManager().getDefaultRank() == rank) {
            player.sendMessage(CC.translate("&4Error! &cYou cannot grant this range as the default."));
            return;
        }
        if (targetPlayerData.hasRank(rank)) {
            player.sendMessage(CC.translate("&4Error! &cThat player already has that rank."));
            return;
        }
        if (!senderData.canGrant(targetPlayerData, rank) && !senderData.hasPermission("core.rank.grant.all") && !senderData.getPlayer().isOp()) {
            player.sendMessage(CC.translate("&4Error! &cYou cannot give yourself that rank as it is higher than yours."));
            return;
        }
        senderData.setGrantProcedure(new GrantProcedure(targetPlayerData));
        senderData.getGrantProcedure().setRankName(rank.getName());
        senderData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.DURATION);
        senderData.getGrantProcedure().setServer("Global");
        player.closeInventory();
        new GrantDurationMenu(targetPlayerData).openMenu(player);
    }
}
