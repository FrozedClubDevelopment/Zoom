package club.frozed.core.manager.messages;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class MessageManager {

    private final HashMap<UUID, UUID> lastReplied = new HashMap<>();

}
