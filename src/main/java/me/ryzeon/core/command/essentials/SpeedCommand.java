package me.ryzeon.core.command.essentials;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.NumberUtils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import me.ryzeon.core.utils.config.ConfigCursor;
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

    @Command(name = "speed", permission = "core.speed", inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "speed");
        if (args.length == 0) {
            p.sendMessage("§eUsage /" + cmd.getLabel() + " <fly/walk> amount");
            return;
        }
        switch (args[0]) {
            case "fly": {
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                int numero = Integer.parseInt(args[1]);
                if (numero < 1 || numero > 10) {
                    p.sendMessage("§cThe number must be between 1 and | 0");
                    return;
                }
                p.setFlySpeed(numero * 0.1F);
                p.sendMessage(Color.translate(messages.getString("fly").replace("<amount>", String.valueOf(numero))));
                break;
            }
            case "walk": {
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                int numero = Integer.parseInt(args[1]);
                if (numero < 1 || numero > 10) {
                    p.sendMessage("§cThe number must be between 1 and | 0");
                    return;
                }
                p.setWalkSpeed(numero * 0.1F);
                p.sendMessage(Color.translate(messages.getString("walk").replace("<amount>", String.valueOf(numero))));
                break;
            }
            default:
                break;
        }
    }
}
