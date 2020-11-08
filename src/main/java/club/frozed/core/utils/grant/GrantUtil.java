package club.frozed.core.utils.grant;

import club.frozed.core.manager.player.grants.Grant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static String getDate(long value) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date(value));
    }
}
