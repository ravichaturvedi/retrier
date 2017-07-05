package io.retrier.handler;


import io.retrier.Logger;

import java.util.function.Supplier;

public abstract class AbstractLoggable implements Loggable {

    protected volatile Logger logger;
    private final String logPrefix;

    public AbstractLoggable() {
        this.logPrefix = String.format("%s: ", this.getClass().getName());
    }

    @Override
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    protected void log(Supplier<String> msgSupplier) {
        if (this.logger != null) {
            this.logger.log(logPrefix + msgSupplier.get());
        }
    }
}
