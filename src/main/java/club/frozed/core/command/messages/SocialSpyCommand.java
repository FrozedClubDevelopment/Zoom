package club.frozed.zoom.command.messages;

import club.frozed.zoom.manager.player.PlayerData;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.lang.Lang;
import org.bukkit.entity.Player;

public class SocialSpyCommand extends BaseCMD {
    @Command(name = "socialSpy", permission = "core.chat.socialSpy", aliases = {"ssspy"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());

        if (data == null) return;
        String status = !data.isSocialSpy() ? "§aenabled" : "§cdisabled";

        if (args.length == 0) {
            Lang.playSound(p, !data.isSocialSpy());
            data.setSocialSpy(!data.isSocialSpy());
            p.sendMessage("§eYou " + status + " §esocial spy.");
        }
    }
}
