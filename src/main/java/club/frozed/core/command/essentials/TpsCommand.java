package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpsCommand extends BaseCommand {
    @Command(name = "servertps", permission = "core.essentials.tps", aliases = {"serverlag", "serverhealth"})

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
        message = CC.translate(message);
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
        message = CC.translate(message);
        message = message
                .replace("<name>", world.getName())
                .replace("<chunks>", String.valueOf(world.getLoadedChunks().length))
                .replace("<entities>", String.valueOf(world.getEntities().size()));
        return message;
    }
}
