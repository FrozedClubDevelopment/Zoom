package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpsCommand extends BaseCMD {
    @Command(name = "tps", permission = "core.essentials.tps", aliases = {"lag", "serverlag", "lagg"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "NETWORK.SERVER-TPS");
        List<String> tps = new ArrayList<>();
        List<String> worlds = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            worlds.add(translateWorld(messages.getString("WORLD-MESSAGE"), world));
        }
        for (String msg : messages.getStringList("MESSAGE")) {
            tps.add(translate(msg, worlds));
        }
        if (args.length == 0) {
            p.sendMessage(StringUtils.join(tps, "\n"));
        }
    }

    public String translate(String message, List<String> strings) {
        message = Color.translate(message);
        message = message
                .replace("<tps>", Utils.getTps())
                .replace("<uptime>", Utils.getUptime())
                .replace("<maxmemory>", String.valueOf(Utils.getMaxMemory()))
                .replace("<allmemory>", String.valueOf(Utils.getAllMemory()))
                .replace("<freememory>", String.valueOf(Utils.getFreeMemory()))
                .replace("<world>", StringUtils.join(strings, "\n"));
        return message;
    }

    public String translateWorld(String message, World world) {
        message = Color.translate(message);
        message = message
                .replace("<name>", world.getName())
                .replace("<chunks>", String.valueOf(world.getLoadedChunks().length))
                .replace("<entities>", String.valueOf(world.getEntities().size()));
        return message;
    }
}
