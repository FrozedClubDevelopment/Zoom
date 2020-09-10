package club.frozed.core.command.essentials;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.Payload;
import club.frozed.core.manager.database.redis.payload.RedisMessage;
import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.manager.staff.StaffLang;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.time.Cooldown;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 9/09/2020 @ 16:07
 * Template by Elb1to
 */

public class ReportCommand extends BaseCMD {
    @Command(name = "report",permission = "core.essentials.report",inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        PlayerData playerData = PlayerData.getByUuid(p.getUniqueId());
        Cooldown cooldown = new Cooldown(Zoom.getInstance().getSettingsConfig().getConfig().getInt("SETTINGS.REPORT.COOLDOWN"));

        if (args.length == 0){
            p.sendMessage(Color.translate("&eUsage /" + cmd.getLabel() + " <player> <message>"));
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null){
            p.sendMessage("§cCouldn't find player");
            return;
        }
        if (targetPlayer == p){
            p.sendMessage(Color.translate("&cYou can't report yourself"));
            return;
        }
        if (args.length < 2){
            p.sendMessage(Color.translate("&cPlease specify a message"));
            return;
        }
        if (!playerData.getReportCooldown().hasExpired()){
            p.sendMessage(Color.translate(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.REPORT.MSG.COOLDOWN")
                    .replace("<time>",playerData.getReportCooldown().getTimeMilisLeft())
                    .replace("<left>",playerData.getReportCooldown().getContextLeft())));
            return;
        }
        playerData.setReportCooldown(cooldown);
        String text = StringUtils.join(args, ' ', 1, args.length);

        p.sendMessage(Color.translate(Zoom.getInstance().getSettingsConfig().getConfig().getString("SETTINGS.REPORT.MSG.SENDER")
                .replace("<target>",targetPlayer.getName())
                .replace("<text>",text)));

        if (Zoom.getInstance().getRedisManager().isActive()){
            String json = new RedisMessage(Payload.REPORT)
                    .setParam("SENDER",p.getName())
                    .setParam("TARGET",targetPlayer.getName())
                    .setParam("SERVER",Lang.SERVER_NAME)
                    .setParam("REASON", text).toJSON();
            Zoom.getInstance().getRedisManager().write(json);
        } else {
            StaffLang.sendReport(p.getName(),targetPlayer.getName(),Lang.SERVER_NAME,text);
        }
    }
}
