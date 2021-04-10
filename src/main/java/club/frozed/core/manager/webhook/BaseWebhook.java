package club.frozed.core.manager.webhook;

import lombok.AllArgsConstructor;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 10/04/2021 @ 02:09
 * Twitter: @Ryzeon_ 😎
 * OnlyFans: onlyfans.com/Ryzeon 😈 🥵🥵🥵
 */

@AllArgsConstructor
public abstract class BaseWebhook {

    private final String webHookName;
    private final String url;

    public abstract void send();
}
