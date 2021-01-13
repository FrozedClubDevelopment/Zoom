package club.frozed.core.manager.event.freeze;

import club.frozed.lib.event.PlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/01/2021 @ 08:54 p. m.
 */

@Getter
@Setter
public class PlayerJoinFreezeEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;

    /***
     *
     * @param player
     */
    public PlayerJoinFreezeEvent(Player player) {
        super(player);
    }
}
