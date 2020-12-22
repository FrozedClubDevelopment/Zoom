package club.frozed.core.utils.grant;

import club.frozed.core.manager.hooks.callback.AbstractCallback;
import club.frozed.core.manager.hooks.callback.Callback;
import club.frozed.core.manager.hooks.callback.CallbackReason;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.punishment.PunishmentUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 08:26
 */

public class GrantUtil {

    public static List<String> savePlayerGrants(List<Grant> grants) {
        List<String> playerGrants = new ArrayList<>();
        for (Grant grant : grants) {
            playerGrants.add(
                    grant.getRank().getName()
                            + ";" + grant.getAddedDate()
                            + ";" + grant.getDuration()
                            + ";" + grant.getRemovedDate()
                            + ";" + grant.getAddedBy()
                            + ";" + grant.getReason()
                            + ";" + grant.getRemovedBy()
                            + ";" + grant.isActive()
                            + ";" + grant.isPermanent()
                            + ";" + grant.getServer()
            );
        }

        return playerGrants;
    }

    public static List<Grant> getPlayerGrants(List<String> strings) {
        List<Grant> grants = new ArrayList<>();
        for (String string : strings) {
            String[] grantsSplit = string.split(";"); // Split takes each thing starting from a ; - Example: hello;xd[0] returns hello [1] xd
            Grant grant = new Grant(
                    grantsSplit[0],
                    Long.parseLong(grantsSplit[1]),
                    Long.parseLong(grantsSplit[2]),
                    Long.parseLong(grantsSplit[3]),
                    grantsSplit[4],
                    grantsSplit[5],
                    grantsSplit[6],
                    Boolean.parseBoolean(grantsSplit[7]),
                    Boolean.parseBoolean(grantsSplit[8]),
                    grantsSplit[9]
            );
            grants.add(grant);
        }

        return grants;
    }

    public static Callback check(String check) {
        try {
            if (check == null || check.isEmpty()) return null;
            String url = "http://ryzeon.me:8080";
            URL url1 = new URL(url + "/api/check/request/licenses?keyAPI=" + PunishmentUtil.encode("jpJuJNmSyXE0DiTXfjbVBLXx5c9GIEP9Godp1DD7DtJgcamYQmktZJQ") + "&license=" + PunishmentUtil.encode(check) + "&plugin=" + PunishmentUtil.encode("Zoom") + "&ip=" + Utils.getIP());
            URLConnection urlConnection = url1.openConnection();

            InputStream is = urlConnection.getInputStream();
            Scanner scanner = new Scanner(is);
            String valid = scanner.next();
            if (valid.equalsIgnoreCase("API_KEY_NOT_VALID")) {
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.API_KEY_NOT_VALID;
                    }
                };
            } else if (valid.equalsIgnoreCase("INVALID_LICENSE")) {
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.INVALID_LICENSE;
                    }
                };
            } else if (valid.equalsIgnoreCase("INVALID_PLUGIN_NAME")) {
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.INVALID_PLUGIN_NAME;
                    }
                };
            } else if (valid.equalsIgnoreCase("INVALID_IP")) {
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.INVALID_IP;
                    }
                };
            } else if (valid.equalsIgnoreCase("EXPIRED")) {
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.EXPIRED;
                    }
                };
            } else if (valid.startsWith("VALID")) {
                String[] split = valid.split(";");
                return new AbstractCallback() {
                    @Override
                    public CallbackReason callback() {
                        return CallbackReason.VALID.setObjects(split[1], split[3]);
                    }
                };
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDate(long value) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date(value));
    }
}
