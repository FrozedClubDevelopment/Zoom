package club.frozed.core.utils.grant;

import club.frozed.core.manager.player.grants.Grant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 08:26
 */

public class GrantUtil {

    public static List<String> savePlayerGrants(List<Grant> grants){
        List<String> playerGrants = new ArrayList<>();
        for (Grant grant : grants){
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
                    + ";" + grant.getServer());
        }
        return playerGrants;
    }

    public static List<Grant> getPlayerGrants(List<String> strings){
        List<Grant> grants = new ArrayList<>();
        for (String string : strings){
            String[] grantsSplit = string.split(";");
            /*
            El split pa agarra cada wea a partir de un ; ekem hola;xd [0] es hola [1] xd
             */
            Grant grant = new Grant(
                    grantsSplit[0],
                    Long.valueOf(grantsSplit[1]),
                    Long.valueOf(grantsSplit[2]),
                    Long.valueOf(grantsSplit[3]),
                    grantsSplit[4],
                    grantsSplit[5],
                    grantsSplit[6],
                    Boolean.valueOf(grantsSplit[7]),
                    Boolean.valueOf(grantsSplit[8]),
                    grantsSplit[9]);
            grants.add(grant);
        }
        return grants;
    }
}
