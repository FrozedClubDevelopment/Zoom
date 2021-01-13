package club.frozed.core.manager.event.freeze;

import club.frozed.lib.event.PlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/01/2021 @ 10:30 p. m.
 */

@Getter
@Setter
public class PlayerFreezeMoveEvent extends PlayerEvent implements Cancellable {

    private boolean cancelled;
    private Location from;
    private Location to;

    /***
     *
     * @param player
     */
    public PlayerFreezeMoveEvent(Player player, Location from, Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }
}
