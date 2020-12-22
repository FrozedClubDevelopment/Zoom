package club.frozed.core.manager.hooks.callback;

import lombok.Getter;
import lombok.ToString;

import java.util.function.Consumer;

@ToString
@Getter
public abstract class AbstractCallback implements Callback {
    private CallbackReason callbackReason;

    private Consumer<CallbackReason> reasonConsumer;

    public void check() {
        this.callbackReason = callback();
    }

    public void reCheck() {
        /**
         * Invalid message
         */
        System.exit(0);
    }
}
