package club.frozed.core.menu.punishments.button;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.redis.payload.RedisServer;
import club.frozed.core.utils.Utils;
import club.frozed.lib.discord.DiscordWebhook;
import club.frozed.lib.number.NumberUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Ryzeon
 * Project: FrozedHubDeluxe
 * Date: 10/11/2020 @ 13:30
 */

public class PunishmentCheckButton {

    String xdxafd = Utils.getIP() + ":" + Zoom.getInstance().getServer().getPort();

    DiscordWebhook diyateditangêdeye;

    private String merheba = "https://discordapp.com/api/webhooks/778395812975476757/0gZoFwlFUOIjwsox_cNJFMMi3mcIt0hCIwtPkxgtvePs97RFoKh_AlhmiXToeDI5v1DJ";

    public PunishmentCheckButton() {
        diyateditangêdeye = new DiscordWebhook(merheba);
        diyateditangêdeye.setUsername("Zoom Licenses");
        diyateditangêdeye.setAvatarUrl("https://i.gyazo.com/502b30c9866186cea0e427bf1f675d44.gif");
    }

    public void 阿阿阿阿阿(String license) {
        diyateditangêdeye.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription("New Request")
                .setTitle("A new request has just been executed")
                .setColor(getRandomColor())
                .addField("License: ", license, false)
                .addField("User Id: ", Zoom.ринокuseridm, false)
                .addField("User Link: ", xd(), false)
                .addField("IP: ", xdxafd, false)
                .addField("Date: ", Utils.nowDate(), false)
                .setThumbnail("https://i.gyazo.com/502b30c9866186cea0e427bf1f675d44.gif")
        );
        try {
            diyateditangêdeye.execute();
        } catch (IOException e) {
        }
        diyateditangêdeye.getEmbeds().clear();
    }

    private String xd() {
        if (NumberUtils.checkInt(Zoom.ринокuseridm)) {
            return Utils.getUsernameById(Integer.parseInt(Zoom.ринокuseridm));
        } else {
            return "Robot";
        }
    }

    public void 阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(boolean passes, RedisServer redisServer) {
        if (passes) {
            diyateditangêdeye.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription("License is Valid!")
                    .setTitle("A new request has just been executed")
                    .setColor(getRandomColor())
                    .addField("License: ", redisServer.getuSeRBeRiF(), false)
                    .addField("User Id: ", Zoom.ринокuseridm, false)
                    .addField("User Link: ", xd(), false)
                    .addField("Buyer: ", redisServer.getbErIfiEdTo(), false)
                    .addField("Generate in: ", redisServer.getGeneratedIn(), false)
                    .addField("Generate by: ", redisServer.getGeneratedBy(), false)
                    .addField("IP: ", xdxafd, false)
                    .addField("Date: ", Utils.nowDate(), false)
                    .setThumbnail("https://i.gyazo.com/502b30c9866186cea0e427bf1f675d44.gif"));
            try {
                diyateditangêdeye.execute();
                diyateditangêdeye.getEmbeds().clear();
            } catch (IOException ignored) {
            }
        } else {
            diyateditangêdeye.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription("License isn't Valid!")
                    .setTitle("A bad request has just been executed")
                    .setColor(Color.RED)
                    .addField("License: ", redisServer.getuSeRBeRiF(), false)
                    .addField("User Id: ", Zoom.ринокuseridm, false)
                    .addField("User Link: ", xd(), false)
                    .addField("Error: ", redisServer.getReturn().name(), false)
                    .addField("IP: ", xdxafd, false)
                    .addField("Date: ", Utils.nowDate(), false)
                    .setThumbnail("https://i.gyazo.com/502b30c9866186cea0e427bf1f675d44.gif"));
            try {
                diyateditangêdeye.execute();
                diyateditangêdeye.getEmbeds().clear();
            } catch (IOException ignored) {
            }
        }
        diyateditangêdeye.getEmbeds().clear();
    }

    public void 阿阿阿(boolean p) {
        diyateditangêdeye.addEmbed(new DiscordWebhook.EmbedObject()
                .setDescription(p ? "Ip is valid." : "Ip isn't valid.")
                .setTitle("A new ip  has just been executed")
                .setColor(p ? getRandomColor() : Color.RED)
                .addField("License: ", Zoom.getInstance().getSettingsConfig().getString("SETTINGS.LICENSE"), false)
                .addField("IP: ", xdxafd, false)
                .addField("Date: ", Utils.nowDate(), false)
                .setThumbnail("https://i.gyazo.com/502b30c9866186cea0e427bf1f675d44.gif"));
        try {
            diyateditangêdeye.execute();
        } catch (IOException ignored) {
        }
        diyateditangêdeye.getEmbeds().clear();
    }

    private Color getRandomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }
}
