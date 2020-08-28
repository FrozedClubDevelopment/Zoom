package club.frozed.zoom.command.essentials;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCMD {
    @Command(name = "heal", permission = "core.essentials.heal", aliases = {"curar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "COMMANDS.HEAL-MESSAGES");
        if (args.length == 0) {
            p.setHealth(p.getMaxHealth());
            p.sendMessage(Color.translate(messages.getString("DEFAULT")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setHealth(target.getMaxHealth());
                p.sendMessage(Color.translate(messages.getString("OTHER").replace("<target>", target.getDisplayName())));
            }
        }
    }
}
