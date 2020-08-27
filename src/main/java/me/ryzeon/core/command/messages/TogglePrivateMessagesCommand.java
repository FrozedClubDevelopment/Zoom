package me.ryzeon.core.command.messages;

import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.lang.Lang;
import org.bukkit.entity.Player;

public class TogglePrivateMessagesCommand extends BaseCMD {
    @Command(name = "togglemessages", aliases = {"togglepm", "tpm"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
        if (data == null) return;
        String status = !data.isToggleprivatemessages() ? "§aenabled" : "§cdisabled";
        if (args.length == 0) {
            Lang.playSound(p, !data.isToggleprivatemessages());
            data.setToggleprivatemessages(!data.isToggleprivatemessages());
            p.sendMessage("§eYou " + status + " §eprivate messages.");
        }
    }
}
