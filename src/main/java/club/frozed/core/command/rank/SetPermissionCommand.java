package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 20:43
 */

public class SetPermissionCommand extends BaseCommand {
    @Command(name = "setpermission", permission = "core.rank.setperm", aliases = {"setperm"},inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender player = cmd.getSender();
        String[] args = cmd.getArgs();

        if (commandGetterWithThreeArgs(player,args,cmd)) return;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (offlinePlayer.isOnline()) {
            PlayerData playerData = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            setPermission(player, playerData, args[1], args[2].equalsIgnoreCase("true"));
            playerData.loadPermissions(playerData.getPlayer());
        } else {
            player.sendMessage(CC.translate("&eLoading player data....."));
            PlayerData targetData = PlayerData.loadData(offlinePlayer.getUniqueId());
            setPermission(player, targetData, args[1], args[2].equalsIgnoreCase("true"));
        }
    }

    private boolean commandGetterWithThreeArgs(CommandSender player, String[] args, CommandArgs commandArgs) {
        if (args.length == 0){
            player.sendMessage(CC.translate("&e/" + commandArgs.getLabel() + " <player> <permission> <true/false>"));
            return true;
        }
        if (args.length < 1){
            player.sendMessage(CC.translate("&cSpecific a player."));
            return true;
        }
        if (args.length < 2){
            player.sendMessage(CC.translate("&cSpecific a permission."));
            return true;
        }
        if (args.length < 3){
            player.sendMessage(CC.translate("&etrue or false"));
            return true;
        }
        return false;
    }

    private void setPermission(CommandSender sender, PlayerData playerData, String permission, boolean set) {
        if (set) {
            if (playerData.hasPermission(permission)) {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&cError! &7That player already has " + permission + " permission."));
                return;
            }
            playerData.getPermissions().add(permission);
            sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added " + permission + " to " + playerData.getName()));
        } else {
            if (!playerData.hasPermission(permission)) {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&cError! &7That player don't have " + permission + " permission."));
                return;
            }
            playerData.getPermissions().remove(permission);
            sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Removed " + permission + " to " + playerData.getName()));
        }
        Player target = Bukkit.getPlayer(playerData.getName());
        if (target == null){
            String json = new RedisMessage(Payload.PLAYER_PERMISSION_UPDATE).setParam("NAME",playerData.getName()).setParam("PERMISSION",permission).toJSON();
            if (Zoom.getInstance().getRedisManager().isActive()) {
                Zoom.getInstance().getRedisManager().write(json);
            }
        }
        if (playerData.isOnline()){
            playerData.saveData();
        } else {
            PlayerData.deleteOfflineProfile(playerData);
        }
    }
}
