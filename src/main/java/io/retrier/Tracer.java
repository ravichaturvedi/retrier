package io.retrier;


@FunctionalInterface
public interface Tracer {

    void trace(String msg);
}
