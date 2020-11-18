package club.frozed.core.command.messages;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TogglePrivateMessagesCommand extends BaseCommand {
    @Command(name = "togglemessages", aliases = {"togglepm", "tpm"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();

        PlayerData data = PlayerData.getPlayerData(p.getUniqueId());
        if (data == null) return;

        String status = !data.isTogglePrivateMessages() ? "§aenabled" : "§cdisabled";
        if (args.length == 0) {
            Lang.playSound(p, !data.isTogglePrivateMessages());
            data.setTogglePrivateMessages(!data.isTogglePrivateMessages());
            p.sendMessage("§eYou " + status + " §eprivate messages.");
        }
    }
}
