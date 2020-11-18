package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {
    @Command(name = "heal", permission = "core.essentials.heal", aliases = {"curar"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.HEAL-MESSAGES");
        if (args.length == 0) {
            p.setHealth(p.getMaxHealth());
            p.sendMessage(CC.translate(messages.getString("DEFAULT")));
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setHealth(target.getMaxHealth());
                p.sendMessage(CC.translate(messages.getString("OTHER").replace("<target>", target.getDisplayName())));
            }
        }
    }
}
