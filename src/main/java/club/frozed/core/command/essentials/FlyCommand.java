package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCMD {
    @Command(name = "fly", permission = "core.essentials.fly", aliases = {"flying"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        /*if (args.length == 0) {
            String status = !p.isFlying() ? "§aenabled" : "§cdisabled";
            p.setAllowFlight(!p.getAllowFlight());
            p.setFlying(!p.isFlying());

            p.sendMessage(Color.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.FLY-MESSAGE").replace("<status>", status)));
        }*/

        if (args.length == 0) {
            p.setAllowFlight(!p.getAllowFlight());
            p.sendMessage(Color.translate(Lang.PREFIX + "&7You have " + (p.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &7your flight mode."));
            return;
        }

        if (args.length == 1) {
            p.sendMessage(Color.translate("&cUsage: /fly <target>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            target.setAllowFlight(!target.getAllowFlight());
            p.sendMessage(Color.translate(Lang.PREFIX + "&7You have " + (target.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &b" + p.getName() + "'s &7flight mode."));
            target.sendMessage(Color.translate(Lang.PREFIX + "&7Your flight mode has been " + (target.getAllowFlight() ? "&aenabled" : "&cdisabled") + " &7by " + p.getName() + "."));
        } else {
            p.sendMessage(Color.translate(Lang.PREFIX + "&cPlayer not found."));
        }
    }
}
