package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CountryCommand extends BaseCMD {
    @Command(name = "getcountry", permission = "core.essentials.geoip", aliases = {"geoip"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "");
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
            p.sendMessage(Color.translate(configCursor.getString("geoip").replace("<player>", playerData.getPlayer().getName()).replace("<country>", Utils.getCountry(ip))));
        } catch (Exception exception) {
            p.sendMessage("§eError in get player country");
        }
    }
}
