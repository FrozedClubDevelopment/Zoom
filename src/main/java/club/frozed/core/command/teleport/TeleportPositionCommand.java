package club.frozed.zoom.command.teleport;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.NumberUtils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPositionCommand extends BaseCMD {
    @Command(name = "teleportposition", permission = "core.command.tppos", aliases = {"tppos"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        if (args.length < 3) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <x> <y> <z>");
            return;
        }
        if (!NumberUtils.checkNumber(args[0]) || !NumberUtils.checkNumber(args[1]) || !NumberUtils.checkNumber(args[2])) {
            p.sendMessage("§ePlease specify a number");
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
        p.sendMessage(Color.translate(messages.getString("TPPOS")
                .replace("<x>", String.valueOf(x))
                .replace("<y>", String.valueOf(y))
                .replace("<z>", String.valueOf(z)))
        );
    }
}
