package club.frozed.core.command.coins;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CoinsCommand extends BaseCMD {
    @Command(name = "coins",aliases = {"monedas"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        PlayerData playerData = PlayerData.getByUuid(p.getUniqueId());
        String[] args = cmd.getArgs();

        if (args.length == 0){
            p.sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.COINS-MESSAGE.DEFAULT")
                    .replace("<amount>",String.valueOf(playerData.getCoins()))));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null){
            p.sendMessage(Lang.OFFLINE_PLAYER);
            return;
        }
        p.sendMessage(CC.translate(Zoom.getInstance().getMessagesConfig().getConfig().getString("COMMANDS.COINS-MESSAGE.TARGET")
                .replace("<target>",target.getName())
                .replace("<amount>",String.valueOf(playerData.getCoins()))));
    }
}
