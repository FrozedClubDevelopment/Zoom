package club.frozed.core.command.coins;

import club.frozed.core.Zoom;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.NumberUtils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.config.ConfigCursor;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CoinsManagerCommand extends BaseCMD {
    @Command(name = "coinsmanager",permission = "core.coins.manager")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.COINS-MESSAGE.MANAGER");

        if (args.length == 0){
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§e/"+cmd.getLabel() + " <player> set <amount>");
            p.sendMessage("§e/"+cmd.getLabel() + " <player> add <amount>");
            p.sendMessage("§e/"+cmd.getLabel() + " <player> remove <amount>");
            p.sendMessage(CC.CHAT_BAR);
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null){
            p.sendMessage(Lang.OFFLINE_PLAYER);
            return;
        }
        PlayerData targetData = PlayerData.getByUuid(target.getUniqueId());
        if (targetData == null){
            p.sendMessage(Lang.OFFLINE_PLAYER);
            return;
        }
        int amount;
        switch (args[1]){
            case "set":
                if (!NumberUtils.checkNumber(args[2])){
                    p.sendMessage(Lang.NO_NUMBER);
                    return;
                }
                amount = Integer.parseInt(args[2]);
                p.sendMessage(CC.translate(configCursor.getString("SET")
                        .replace("<target>",target.getName())
                        .replace("<amount>",String.valueOf(amount))));
                targetData.setCoins(amount);
                break;
            case "add":
                if (!NumberUtils.checkNumber(args[2])){
                    p.sendMessage(Lang.NO_NUMBER);
                    return;
                }
                amount = Integer.parseInt(args[2]);
                p.sendMessage(CC.translate(configCursor.getString("ADD-REMOVE")
                        .replace("<target>",target.getName())
                        .replace("<coins>",String.valueOf(targetData.getCoins()))
                        .replace("<amount>",String.valueOf(targetData.getCoins() + amount))));
                targetData.setCoins(targetData.getCoins() + amount);
                break;
            case "remove":
                if (!NumberUtils.checkNumber(args[2])){
                    p.sendMessage(Lang.NO_NUMBER);
                    return;
                }
                amount = Integer.parseInt(args[2]);
                p.sendMessage(CC.translate(configCursor.getString("ADD-REMOVE")
                        .replace("<target>",target.getName())
                        .replace("<coins>",String.valueOf(targetData.getCoins()))
                        .replace("<amount>",String.valueOf(targetData.getCoins() - amount))));
                targetData.setCoins(targetData.getCoins() - amount);
                break;
            default:
                p.sendMessage(CC.CHAT_BAR);
                p.sendMessage("§e/"+cmd.getLabel() + " <player> set <amount>");
                p.sendMessage("§e/"+cmd.getLabel() + " <player> add <amount>");
                p.sendMessage("§e/"+cmd.getLabel() + " <player> remove <amount>");
                p.sendMessage(CC.CHAT_BAR);
                break;
        }
    }
}
