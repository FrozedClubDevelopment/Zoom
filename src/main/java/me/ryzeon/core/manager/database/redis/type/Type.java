package me.ryzeon.core.manager.database.redis.type;

import lombok.Getter;

public enum Type {
    SERVER_ON("SERVER_ON"),
    SERVER_OFF("SERVER_OFF"),
    ADMIN_CHAT("ADMIN_CHAT");

    @Getter
    private String name;

    Type(String name) {
        this.name = name;
    }
}
