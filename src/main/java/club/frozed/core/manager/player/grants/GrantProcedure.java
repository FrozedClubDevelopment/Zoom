package club.frozed.core.manager.player.grants;

import club.frozed.core.manager.player.PlayerData;
import club.frozed.core.utils.Utils;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 20/09/2020 @ 22:29
 * Template by Elp1to
 */

@Getter @Setter
public class GrantProcedure {
    private PlayerData playerData;

    private GrantProcedureState grantProcedureState = GrantProcedureState.START;

    private long enteredDuration;

    private String enteredReason;

    private String rankName;

    private String server;

    private boolean permanent = false;

    public GrantProcedure(PlayerData playerData){
        this.playerData = playerData;
    }

    public String getNiceDuration() {
        if (isPermanent())
            return "Permanent";
        return Utils.formatTimeMillis(this.enteredDuration);
    }
}
