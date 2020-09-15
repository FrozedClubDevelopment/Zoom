package club.frozed.core.command.teleport;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpallCommand extends BaseCMD {
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
