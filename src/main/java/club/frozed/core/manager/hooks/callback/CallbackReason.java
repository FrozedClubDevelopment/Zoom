package club.frozed.core.manager.hooks.callback;

public enum CallbackReason {
    API_KEY_NOT_VALID(5, null, null),
    INVALID_LICENSE(4, null, null),
    INVALID_PLUGIN_NAME(3, null, null),
    INVALID_IP(2, null, null),
    EXPIRED(1, null, null),
    VALID(0, null, null);

    private Object data;
    private Object buyer;
    private Object generateDate;

    public CallbackReason setObjects(Object o1, Object o2) {
        this.buyer = o1;
        this.generateDate = o2;
        return this;
    }

    public Object object1() {
        return this.buyer;
    }

    public Object object2() {
        return this.generateDate;
    }

    CallbackReason(Object data, Object buyer, Object generateDate) {
        this.data = data;
        this.buyer = buyer;
        this.generateDate = generateDate;
    }

    public Object getData(Class<?> type) {
        return type.cast(this.data);
    }
}
