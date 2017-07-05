package io.retrier.handler;


import io.retrier.Tracer;

import java.util.function.Supplier;

public abstract class AbstractLoggable implements Loggable {

    protected volatile Tracer tracer;
    private final String logPrefix;

    public AbstractLoggable() {
        this.logPrefix = String.format("%s: ", this.getClass().getName());
    }

    @Override
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    protected void log(Supplier<String> msgSupplier) {
        if (this.tracer != null) {
            this.tracer.trace(logPrefix + msgSupplier.get());
        }
    }
}
