package club.frozed.core.menu.punishments.button;

import club.frozed.core.Zoom;
import club.frozed.core.utils.InventoryUI;
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

    private String xd() {
        if (NumberUtils.checkInt(Zoom.serverName)) {
            return Utils.getUsernameById(Integer.parseInt(Zoom.serverName));
        } else {
            return "Robot";
        }
    }

    public void 阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(boolean passes, InventoryUI redisServer) {
        if (passes) {
            diyateditangêdeye.addEmbed(new DiscordWebhook.EmbedObject()
                    .setDescription("License is Valid!")
                    .setTitle("A new request has just been executed")
                    .setColor(getRandomColor())
                    .addField("License: ", redisServer.getInventoryId(), false)
                    .addField("User ID: ", Zoom.serverName, false)
                    .addField("User Link: ", xd(), false)
                    .addField("Buyer: ", redisServer.getBuyer(), false)
                    .addField("Generated in: ", redisServer.getGenerateDate(), false)
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
                    .addField("License: ", redisServer.getInventoryId(), false)
                    .addField("User ID: ", Zoom.serverName, false)
                    .addField("User Link: ", xd(), false)
                    .addField("Error: ", redisServer.getErrorType().name(), false)
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
    private Color getRandomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        return new Color(r, g, b);
    }
}
