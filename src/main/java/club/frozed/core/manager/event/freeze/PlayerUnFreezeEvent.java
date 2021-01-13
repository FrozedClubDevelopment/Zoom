package club.frozed.core.manager.event.freeze;

import club.frozed.lib.event.PlayerEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 10/01/2021 @ 05:28 p. m.
 */

@Getter
@Setter
public class PlayerUnFreezeEvent extends PlayerEvent implements Cancellable {

    private Player sender;

    private boolean cancelled;

    /***
     *
     * @param player
     * @param sender
     */
    public PlayerUnFreezeEvent(Player player, Player sender) {
        super(player);
        this.sender = sender;
    }
}
