package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CountryCommand extends BaseCMD {
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

//        playerData = PlayerData.getByUuid(Bukkit.getPlayer(args[0]).getUniqueId());
        playerData = PlayerData.getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId());
        if (playerData == null) return;
        if (playerData.getCountry() == null) return;
        String ip = playerData.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
        try {
            p.sendMessage(CC.translate(configCursor.getString("GEO-IP-MESSAGE")
                    .replace("<player>", playerData.getPlayer().getName())
                    .replace("<country>", Objects.requireNonNull(Utils.getCountry(ip))))
            );
        } catch (Exception exception) {
            p.sendMessage("§eError in get player country");
        }
    }
}
