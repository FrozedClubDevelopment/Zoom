package club.frozed.zoom.command.essentials;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.manager.player.PlayerData;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.Utils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CountryCommand extends BaseCMD {
    @Command(name = "getcountry", permission = "core.essentials.geoip", aliases = {"geoip"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS");
        PlayerData playerData;

        if (args.length == 0) {
            p.sendMessage("§cUsage /" + cmd.getLabel() + " <player>");
            return;
        }

        playerData = PlayerData.getByUuid(Bukkit.getPlayer(args[0]).getUniqueId());
        if (playerData == null) return;
        if (playerData.getCountry() == null) return;
        String ip = playerData.getPlayer().getAddress().getAddress().toString().replaceAll("/", "");
        try {
            p.sendMessage(Color.translate(configCursor.getString("GEO-IP-MESSAGE")
                    .replace("<player>", playerData.getPlayer().getName())
                    .replace("<country>", Objects.requireNonNull(Utils.getCountry(ip))))
            );
        } catch (Exception exception) {
            p.sendMessage("§eError in get player country");
        }
    }
}
