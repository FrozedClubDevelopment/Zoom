package me.ryzeon.core.manager.database.redis.implement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminChatMessage {

    private String playername;
    private String server;
    private String message;
}
