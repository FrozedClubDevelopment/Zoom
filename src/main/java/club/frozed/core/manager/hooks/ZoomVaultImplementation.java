package club.frozed.core.manager.hooks;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.ranks.Rank;
import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import sun.security.acl.PermissionImpl;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 29/09/2020 @ 21:06
 */
@Getter
public class ZoomVaultImplementation extends Permission {

    @Override
    public String getName() {
        return "Zoom";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }

    @Override
    public boolean playerHas(String s, String player, String permission) {
        if (Bukkit.getPlayer(player) == null) return false;
        return Bukkit.getPlayer(player).hasPermission(permission) || Bukkit.getPlayer(player).isOp();
    }

    @Override
    public boolean playerAdd(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerRemove(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return Rank.getRankByName(group).getPermissions().stream().anyMatch(permission::equalsIgnoreCase);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        if (Rank.getRankByName(group) == null) return false;

        Rank.getRankByName(group).getPermissions().add(permission);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        if (Rank.getRankByName(group) == null) return false;

        Rank.getRankByName(group).getPermissions().remove(permission);
        return false;
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {
        if (Rank.getRankByName(group) == null || PlayerData.getByName(player) == null) return false;

        return PlayerData.getByName(player).getActiveGrants().stream().anyMatch(grant -> grant.getRank().getName().equalsIgnoreCase(group));
    }

    public boolean playerAddGroup(String s, String playerName, String group) {
        if (Bukkit.getPlayer(playerName) != null) {
            Rank rank = Rank.getRankByName(group);
            if (rank != null) {
                PlayerData playerData = PlayerData.getByUuid(Bukkit.getPlayer(playerName).getUniqueId());
                if (playerData != null) {
                    Grant grant = new Grant(null, 1L, 1L, 1L, "", "", "", false, false, "Global");
                    grant.setActive(true);
                    grant.setAddedBy("Console");
                    grant.setReason("Added by Vault");
                    grant.setRankName(rank.getName());
                    grant.setPermanent(true);
                    grant.setAddedDate(System.currentTimeMillis());
                    playerData.getGrants().add(grant);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean playerRemoveGroup(String s, String playerName, String group) {
        if (Bukkit.getPlayer(playerName) != null) {
            PlayerData playerData = PlayerData.getByUuid(Bukkit.getPlayer(playerName).getUniqueId());
            if (playerData != null) {
                for (Grant grant : playerData.getGrants()) {
                    if (grant.getRankName().equalsIgnoreCase(group)) {
                        grant.setActive(false);
                        grant.setRemovedDate(System.currentTimeMillis());
                        grant.setRemovedBy("Console [Vault]");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {
        Collection<String> result;
        result = PlayerData.getByName(player).getActiveGrants().stream().map(grant -> grant.getRank().getName()).collect(Collectors.toList());
        return (String[]) result.toArray();
    }

    @Override
    public String getPrimaryGroup(String world, String player) {
        if (PlayerData.getByName(player) == null) return "no_data";

        return PlayerData.getByName(player).getHighestRank().getName();
    }

    @Override
    public String[] getGroups() {
        return (String[]) Rank.getRanks().stream().map(Rank::getName).toArray();
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }

    public void register() {
        for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(Permission.class)) {
            if (Permission.class.equals(provider.getService()) && provider.getPlugin().getName().equals("Vault") && PermissionImpl.class.isAssignableFrom(provider.getProvider().getClass()) && provider.getPriority() == ServicePriority.Highest) {
                Zoom.getInstance().getLogger().warning("Removing default vault permission hook");
                Bukkit.getServicesManager().unregister(Permission.class, provider.getProvider());
            }
        }
    }
}
