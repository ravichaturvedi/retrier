/*
 * Copyright 2017 The Retrier AUTHORS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ravichaturvedi.retrier.handler.limit;

import io.github.ravichaturvedi.retrier.Handler;
import io.github.ravichaturvedi.retrier.handler.Traceable;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.ravichaturvedi.retrier.helper.Ensurer.ensure;

/**
 * {@link ExpBackoffHandler} is a {@link Handler} implementation to handle using exponential backoff delay.
 */
public class ExpBackoffHandler extends Traceable implements Handler {

    // Initial delay in milliseconds to backoff
    private final long initialDelayInMillisec;

    // Max delay in milliseconds to backoff
    private final Long maxDelayInMillisec;

    // Keeping track of retry count to compute exponential backoff
    private final AtomicInteger retryCount;

    public ExpBackoffHandler(long initialDelayInMillisec, Long maxDelayInMillisec) {
        ensure(initialDelayInMillisec > 0, "Initial delay should be positive.");
        if (maxDelayInMillisec != null) {
            ensure(maxDelayInMillisec > 0, "Max delay should be positive.");
        }

        this.initialDelayInMillisec = initialDelayInMillisec;
        this.maxDelayInMillisec = maxDelayInMillisec;
        this.retryCount = new AtomicInteger(0);
    }

    public ExpBackoffHandler(long initialDelayInMillisec) {
        this(initialDelayInMillisec, null);
    }

    @Override
    public void handlePreExec() {
        this.retryCount.incrementAndGet();
    }

    @Override
    public void handleException(Exception e) throws Exception {
        if (maxDelayInMillisec != null) {
            handleWithMaxDelay();
            return;
        }

        sleep(delay());
    }

    private void handleWithMaxDelay() throws Exception {
        long delay = delay();
        if (delay > maxDelayInMillisec) {
            sleep(maxDelayInMillisec);
            return;
        }

        sleep(delay);
    }

    private long delay() {
        return initialDelayInMillisec * (long)Math.pow(2, retryCount.get() - 1);
    }

    private void sleep(long durationInMillisec) throws Exception {
        try {
            trace(() -> String.format("Sleeping for %s", Duration.ofMillis(durationInMillisec)));
            Thread.sleep(durationInMillisec);
        } catch (InterruptedException e) {
            trace(() -> String.format("Sleeping thread interrupted: %s", e.getMessage()));
            throw e;
        }
    }
}
