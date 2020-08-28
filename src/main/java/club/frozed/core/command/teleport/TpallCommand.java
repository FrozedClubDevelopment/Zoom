package club.frozed.zoom.command.teleport;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.Utils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpallCommand extends BaseCMD {
    @Command(name = "teleportall", permission = "core.command.tpall", aliases = {"tpall"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor message = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS.TELEPORT-MESSAGES");

        for (Player player : Bukkit.getOnlinePlayers()) player.teleport(p.getLocation());
        Utils.sendAllMsg(Color.translate(message.getString("TPALL").replace("<player>", p.getDisplayName())));
    }
}
