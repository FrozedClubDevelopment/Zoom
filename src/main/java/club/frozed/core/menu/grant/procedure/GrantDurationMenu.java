package club.frozed.core.menu.grant.procedure;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.grants.GrantProcedureState;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.grant.WoolUtil;
import club.frozed.lib.item.ItemCreator;
import club.frozed.lib.menu.Button;
import club.frozed.lib.menu.Menu;
import club.frozed.core.utils.time.DateUtils;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 23:49
 */
public class GrantDurationMenu extends Menu {

    private PlayerData targetData;

    private boolean custom;

    private boolean completed;

    public GrantDurationMenu(PlayerData playerData) {
        this.targetData = playerData;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&aChoose duration");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new DurationButton("14d"));
        buttons.put(12, new DurationButton("30d"));
        buttons.put(14, new DurationButton("Perm"));
        buttons.put(16, new DurationButton("Custom"));

        setPlaceholder(true);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public void onClose(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.getGrantProcedure() != null && !this.custom && !this.completed) {
            playerData.setGrantProcedure(null);
        }
        PlayerData.deleteOfflineProfile(this.targetData.getUuid());
    }

    @AllArgsConstructor
    private class DurationButton extends Button {

        String type;

        @Override
        public ItemStack getButtonItem(Player player) {
            return getItem(type);
        }

        private ItemStack getItem(String type) {
            ItemStack itemStack = null;
            switch (type) {
                case "14d":
                    itemStack = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.GREEN)).setName("&a&l14 Days").get();
                    break;
                case "30d":
                    itemStack = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.YELLOW)).setName("&6&l30 Days").get();
                    break;
                case "Perm":
                    itemStack = new ItemCreator(Material.WOOL).setDurability(WoolUtil.convertChatColorToWoolData(ChatColor.RED)).setName("&4&lPermanent").get();
                    break;
                case "Custom":
                    itemStack = new ItemCreator(Material.SIGN).setName("&9&lCustom Duration").get();
                    break;
            }
            return itemStack;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            PlayerData data = PlayerData.getPlayerData(player.getUniqueId());
            GrantProcedure targetGrantProcedure = data.getGrantProcedure();
            long duration = 1L;
            switch (type) {
                case "14d":
                    if (targetGrantProcedure == null) {
                        player.closeInventory();
                        player.sendMessage(CC.translate("&4Error!"));
                        return;
                    }
                    duration = DateUtils.getDuration("14d");
                    GrantDurationMenu.this.completed = true;
                    data.getGrantProcedure().setEnteredDuration(duration);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    player.closeInventory();
                    player.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSuccess(player);
                    break;
                case "30d":
                    if (targetGrantProcedure == null) {
                        player.closeInventory();
                        player.sendMessage(CC.translate("&4Error!"));
                        return;
                    }
                    duration = DateUtils.getDuration("30d");
                    GrantDurationMenu.this.completed = true;
                    data.getGrantProcedure().setEnteredDuration(duration);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    player.closeInventory();
                    player.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSuccess(player);
                    break;
                case "Perm":
                    if (targetGrantProcedure == null) {
                        player.closeInventory();
                        player.sendMessage(CC.translate("&4Error!"));
                        return;
                    }
                    GrantDurationMenu.this.completed = true;
                    targetGrantProcedure.setPermanent(true);
                    data.getGrantProcedure().setEnteredDuration(1L);
                    data.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                    player.closeInventory();
                    player.sendMessage(CC.translate("&aPlease type a reason for grant."));
                    playSuccess(player);
                    break;
                case "Custom":
                    if (targetGrantProcedure == null) {
                        player.closeInventory();
                        player.sendMessage(CC.translate("&4Error!"));
                        return;
                    }
                    GrantDurationMenu.this.custom = true;
                    player.closeInventory();
                    player.sendMessage(CC.translate("&aPlease specify a duration, you can use perm or permanent for permanent duration"));
                    break;
            }
        }
    }
}
