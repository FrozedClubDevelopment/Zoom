package club.frozed.core.manager.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 24/09/2020 @ 12:40
 */

@Getter
@RequiredArgsConstructor
public class PlayerReportEvent extends Event implements Cancellable {
    private static HandlerList handlers = new HandlerList();

    private final Player player;
    private final String reported;
    private final String reason;

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
