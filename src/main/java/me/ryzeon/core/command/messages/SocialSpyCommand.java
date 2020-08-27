package me.ryzeon.core.command.messages;

import me.ryzeon.core.manager.player.PlayerData;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SocialSpyCommand extends BaseCMD {
    @Command(name = "socialspy", permission = "core.chat.socialspy", aliases = {"ssspy"})
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
        if (data == null) return;
        String status = !data.isSocialspy() ? "§aenabled" : "§cdisabled";
        if (args.length == 0) {
            playSound(p, !data.isToggleprivatemessages());
            data.setSocialspy(!data.isSocialspy());
            p.sendMessage("§eYou " + status + " §esocial spy.");
        }
    }

    public void playSound(Player player, boolean confirmation) {
        if (confirmation) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
        }
    }
}
