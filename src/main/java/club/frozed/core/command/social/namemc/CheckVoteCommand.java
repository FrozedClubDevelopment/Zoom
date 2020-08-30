package club.frozed.core.command.social.namemc;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheckVoteCommand extends BaseCMD {
    @Command(name = "checkvote", aliases = {"namemccheck"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData data = PlayerData.getByUuid(p.getUniqueId());
        String command = Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.NAME-MC-CHECK.EXECUTE-CMD").replace("<player>", p.getName());

        if (args.length == 0) {
            if (data == null) return;
            if (data.isVote()) {
                p.sendMessage("§cYou already voted for the server!");
            } else {
                p.sendMessage("§aChecking.....");
                if (Utils.checkPlayerVote(p.getUniqueId())) {
                    p.sendMessage("§aCorrect verification!");
                    data.setVote(true);
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                } else {
                    p.sendMessage("§cAre you sure you have voted for the server?");
                }
            }
        }
    }
}
