package club.frozed.core.manager.impl;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.lib.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 1/26/2021 @ 8:52 PM
 */
@Getter
public class VaultChatImpl extends net.milkbowl.vault.chat.Chat {

	public VaultChatImpl(net.milkbowl.vault.permission.Permission perms) {
		super(perms);
	}

	@Override
	public String getName() {
		return "Zoom";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPlayerPrefix(String world, String player) {
		PlayerData data = PlayerData.getPlayerData(player);
		if (data == null) {
			return "no_data";
		}

		return data.getHighestRank().getPrefix();
	}

	@Override
	public void setPlayerPrefix(String world, String player, String prefix) {

	}

	@Override
	public String getPlayerSuffix(String world, String player) {
		PlayerData data = PlayerData.getPlayerData(player);
		if (data == null) {
			return "no_data";
		}

		return data.getHighestRank().getSuffix();
	}

	@Override
	public void setPlayerSuffix(String world, String player, String suffix) {

	}

	@Override
	public String getGroupPrefix(String world, String group) {
		Rank rank = Rank.getRankByName(group);
		if (rank == null) {
			return "no_data";
		}

		return rank.getPrefix();
	}

	@Override
	public void setGroupPrefix(String world, String group, String prefix) {
		Rank rank = Rank.getRankByName(group);
		if (rank == null) {
			return;
		}

		rank.setPrefix(prefix);
	}

	@Override
	public String getGroupSuffix(String world, String group) {
		Rank rank = Rank.getRankByName(group);
		if (rank == null) {
			return "no_data";
		}

		return rank.getSuffix();
	}

	@Override
	public void setGroupSuffix(String world, String group, String suffix) {
		Rank rank = Rank.getRankByName(group);
		if (rank == null) {
			return;
		}

		rank.setSuffix(suffix);
	}

	@Override
	public int getPlayerInfoInteger(String world, String player, String node, int defaultValue) {
		return 0;
	}

	@Override
	public void setPlayerInfoInteger(String world, String player, String node, int value) {

	}

	@Override
	public int getGroupInfoInteger(String world, String group, String node, int defaultValue) {
		return 0;
	}

	@Override
	public void setGroupInfoInteger(String world, String group, String node, int value) {

	}

	@Override
	public double getPlayerInfoDouble(String world, String player, String node, double defaultValue) {
		return 0;
	}

	@Override
	public void setPlayerInfoDouble(String world, String player, String node, double value) {

	}

	@Override
	public double getGroupInfoDouble(String world, String group, String node, double defaultValue) {
		return 0;
	}

	@Override
	public void setGroupInfoDouble(String world, String group, String node, double value) {

	}

	@Override
	public boolean getPlayerInfoBoolean(String world, String player, String node, boolean defaultValue) {
		return false;
	}

	@Override
	public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {

	}

	@Override
	public boolean getGroupInfoBoolean(String world, String group, String node, boolean defaultValue) {
		return false;
	}

	@Override
	public void setGroupInfoBoolean(String world, String group, String node, boolean value) {

	}

	@Override
	public String getPlayerInfoString(String world, String player, String node, String defaultValue) {
		return null;
	}

	@Override
	public void setPlayerInfoString(String world, String player, String node, String value) {

	}

	@Override
	public String getGroupInfoString(String world, String group, String node, String defaultValue) {
		return null;
	}

	@Override
	public void setGroupInfoString(String world, String group, String node, String value) {

	}

	public void register() {
		Bukkit.getServer().getServicesManager().register(net.milkbowl.vault.chat.Chat.class, this, Zoom.getInstance(), ServicePriority.Highest);
		Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&b&lZoom&7]" + "&aSuccessfully implemented Vault's chat into Zoom."));
	}
}
