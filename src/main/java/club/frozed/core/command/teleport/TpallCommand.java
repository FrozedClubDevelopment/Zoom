package club.frozed.core.command.teleport;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpallCommand extends BaseCommand {
    @Command(name = "teleportall", permission = "core.command.tpall", aliases = {"tpall"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor message = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        for (Player player : Bukkit.getOnlinePlayers()) player.teleport(p.getLocation());
        Utils.sendAllMsg(CC.translate(message.getString("TPALL").replace("<player>", p.getDisplayName())));
    }
}
