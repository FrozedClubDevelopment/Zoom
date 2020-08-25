package me.ryzeon.core.command.teleport;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.NumberUtils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPosicionCommand extends BaseCMD {
    @Command(name = "teleportposition", permission = "core.command.tppos", aliases = {"tppos"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "teleport");
        if (args.length < 3) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <x> <y> <z>");
            return;
        }
        if (!NumberUtils.checkNumber(args[0]) || !NumberUtils.checkNumber(args[1]) || !NumberUtils.checkNumber(args[2])) {
            p.sendMessage("§eEspecific a number");
            return;
        }
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        Location loc = new Location(p.getWorld(), x, y, z);
        if (x > 30000000 || x < -30000000 || y > 30000000 || y < -30000000 || z > 30000000 || z < -30000000) {
            p.sendMessage("§eMax value is 30000000/-30000000");
            return;
        }
        p.teleport(loc);
        p.sendMessage(Color.translate(messages.getString("tppos")
                .replace("<x>", String.valueOf(x))
                .replace("<y>", String.valueOf(y))
                .replace("<z>", String.valueOf(z))));
    }
}
