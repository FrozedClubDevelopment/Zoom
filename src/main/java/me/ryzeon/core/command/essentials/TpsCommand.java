package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpsCommand extends BaseCMD {
    @Command(name = "tps", permission = "core.tps", aliases = {"lag", "serverlag", "lagg"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "tps");
        List<String> tps = new ArrayList<>();
        List<String> worlds = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            worlds.add(translateWorld(messages.getString("world"), world));
        }
        for (String msg : messages.getStringList("text")) {
            tps.add(translate(msg, worlds));
        }
        if (args.length == 0) {
            p.sendMessage(StringUtils.join(tps, "\n"));
        }
    }

    public String translate(String text, List<String> lista) {
        text = Color.translate(text);
        text = text
                .replace("<tps>", Utils.getTps())
                .replace("<uptime>", Utils.getUptime())
                .replace("<maxmemory>", String.valueOf(Utils.getMaxMemory()))
                .replace("<allmemory>", String.valueOf(Utils.getAllMemory()))
                .replace("<freememory>", String.valueOf(Utils.getFreeMemory()))
                .replace("<world>", StringUtils.join(lista, "\n"));
        return text;
    }

    public String translateWorld(String text, World world) {
        text = Color.translate(text);
        text = text
                .replace("<name>", world.getName())
                .replace("<chunks>", String.valueOf(world.getLoadedChunks().length))
                .replace("<entities>", String.valueOf(world.getEntities().size()));
        return text;
    }
}
