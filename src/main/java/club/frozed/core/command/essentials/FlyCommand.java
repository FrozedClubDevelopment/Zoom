package club.frozed.zoom.command.essentials;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCMD {
    @Command(name = "fly", permission = "core.essentials.fly", aliases = {"flying"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            String status = !p.isFlying() ? "§aenabled" : "§cdisabled";
            p.setAllowFlight(!p.getAllowFlight());
            p.setFlying(!p.isFlying());

            p.sendMessage(Color.translate(ZoomPlugin.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.FLY-MESSAGE")
                    .replace("<status>", status))
            );
        }
    }
}
