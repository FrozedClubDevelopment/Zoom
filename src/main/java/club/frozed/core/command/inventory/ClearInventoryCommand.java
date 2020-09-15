package club.frozed.core.command.inventory;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends BaseCMD {
    @Command(name = "clear", permission = "core.command.clear", usage = "Clear inventory", inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.CLEAR-MESSAGES");

        if (args.length == 0) {
            cleanInventory(p);
            p.sendMessage(CC.translate(messages.getString("DEFAULT")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cPlayers isn't online");
                return;
            }
            cleanInventory(target);
            target.sendMessage(CC.translate(messages.getString("OTHER.TARGET").replace("<player>", p.getDisplayName())));
            p.sendMessage(CC.translate(messages.getString("OTHER.SENDER")).replace("<target>", target.getDisplayName()));
        }
    }

    public void cleanInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
    }
}
