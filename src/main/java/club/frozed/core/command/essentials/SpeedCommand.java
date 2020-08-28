package club.frozed.zoom.command.essentials;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.NumberUtils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.command.Completer;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SpeedCommand extends BaseCMD {

    @Completer(name = "speed")
    public List<String> speedCompleter(CommandArgs args) {
        List<String> add = new ArrayList<>();
        if (args.length() == 1) {
            add.add("fly");
            add.add("walk");
        }
        return add;
    }

    @Command(name = "speed", permission = "core.essentials.speed", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS.SPEED-MESSAGES");

        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <fly/walk> amount");
            return;
        }

        int speedVelocity = Integer.parseInt(args[1]);
        switch (args[0]) {
            case "fly":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                if (speedVelocity < 1 || speedVelocity > 10) {
                    p.sendMessage("§cThe number must be between 1 and 10");
                    return;
                }
                p.setFlySpeed(speedVelocity * 0.1F);
                p.sendMessage(Color.translate(messages.getString("FLY").replace("<amount>", String.valueOf(speedVelocity))));
                break;
            case "walk":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                if (speedVelocity < 1 || speedVelocity > 10) {
                    p.sendMessage("§cThe number must be between 1 and 10");
                    return;
                }
                p.setWalkSpeed(speedVelocity * 0.1F);
                p.sendMessage(Color.translate(messages.getString("WALK").replace("<amount>", String.valueOf(speedVelocity))));
                break;
            default:
                break;
        }
    }
}
