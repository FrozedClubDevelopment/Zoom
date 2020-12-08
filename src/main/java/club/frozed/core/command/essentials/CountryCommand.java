package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CountryCommand extends BaseCommand {
    @Command(name = "getcountry", permission = "core.essentials.geoip", aliases = {"geoip"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS");
        PlayerData playerData;

        if (args.length == 0) {
            p.sendMessage("§cUsage /" + cmd.getLabel() + " <player>");
            return;
        }

        Player offlinePlayer = Bukkit.getPlayer(args[0]);
        if (offlinePlayer == null){
            p.sendMessage(CC.RED + offlinePlayer.getName() + " isn't online.");
            return;
        }
        String ip = offlinePlayer.getAddress().getAddress().toString().replaceAll("/", "");
        try {
            p.sendMessage(CC.translate(configCursor.getString("GEO-IP-MESSAGE")
                    .replace("<player>", offlinePlayer.getName())
                    .replace("<country>", Objects.requireNonNull(Utils.getCountry(ip))))
            );
        } catch (Exception exception) {
            p.sendMessage("§eError in get player country");
        }
    }
}
