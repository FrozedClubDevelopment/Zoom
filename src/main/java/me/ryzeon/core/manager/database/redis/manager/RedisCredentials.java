package me.ryzeon.core.manager.database.redis.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedisCredentials {

    private final String host;
    private final Integer port;

    private String password;

    public RedisCredentials authenticate(String password) {
        this.password = password;
        return (this);
    }

    /**
     * Check if there is a password or the password is not empty.
     *
     * @return If there is a password.
     */
    public boolean hasPassword() {
        return (password != null && !password.isEmpty() && !password.equals(""));
    }

    /**
     * Check if the password have been applied.
     *
     * @return If there is a password.
     */
    public boolean shouldAuthenticate() {
        return (hasPassword());
    }
}
