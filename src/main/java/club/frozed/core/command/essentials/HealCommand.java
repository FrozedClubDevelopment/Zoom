package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCMD {
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
