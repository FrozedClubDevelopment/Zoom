package club.frozed.core.manager.event.freeze;

import club.frozed.lib.event.PlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 10/01/2021 @ 05:31 p. m.
 */

@Getter
@Setter
public class PlayerLeaveFreezeEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;

    /***
     *
     * @param player
     */
    public PlayerLeaveFreezeEvent(Player player) {
        super(player);
    }
}
