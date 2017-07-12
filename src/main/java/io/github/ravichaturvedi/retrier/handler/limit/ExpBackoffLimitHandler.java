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

import io.github.ravichaturvedi.retrier.handler.AbstractTraceable;
import io.github.ravichaturvedi.retrier.utils.Preconditions;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class ExpBackoffLimitHandler extends AbstractTraceable implements LimitHandler {

    private final long initialDelayInMillisec;
    private final Long maxDelayInMillisec;
    private final AtomicInteger retryCount;

    public ExpBackoffLimitHandler(long initialDelayInMillisec, Long maxDelayInMillisec) {
        Preconditions.ensure(initialDelayInMillisec > 0, "Initial delay should be positive.");
        if (maxDelayInMillisec != null) {
            Preconditions.ensure(maxDelayInMillisec > 0, "Max delay should be positive.");
        }

        this.initialDelayInMillisec = initialDelayInMillisec;
        this.maxDelayInMillisec = maxDelayInMillisec;
        this.retryCount = new AtomicInteger(0);
    }

    public ExpBackoffLimitHandler(long initialDelayInMillisec) {
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

    private void handleWithMaxDelay() {
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

    private void sleep(long durationInMillisec) {
        try {
            trace(() -> String.format("Sleeping for %s", Duration.ofMillis(durationInMillisec)));
            Thread.sleep(durationInMillisec);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
