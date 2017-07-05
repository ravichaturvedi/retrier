/*
 * Copyright 2017 Ravi Chaturvedi
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
package io.retrier.handler.limit;

import io.retrier.handler.AbstractTraceable;
import io.retrier.utils.Preconditions;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;


/**
 * {@link TimeoutLimitHandler} is a {@link LimitHandler} implementation to make sure retry is happening within the max timeout provided.
 */
public class TimeoutLimitHandler extends AbstractTraceable implements LimitHandler {

    private final long timeoutInMillisec;
    private final AtomicLong startTimeInMillisec;

    public TimeoutLimitHandler(long timeoutInMillisec) {
        Preconditions.ensure(timeoutInMillisec > 0, "Timeout should be positive.");
        this.timeoutInMillisec = timeoutInMillisec;
        this.startTimeInMillisec = new AtomicLong(0);
    }

    @Override
    public void handlePreExec() {
        startTimeInMillisec.compareAndSet(0, System.currentTimeMillis());
    }

    @Override
    public void handleException(Exception e) throws Exception {
        long elapsedTimeInMillisec = System.currentTimeMillis() - startTimeInMillisec.get();
        if (elapsedTimeInMillisec > timeoutInMillisec) {
            trace(() -> String.format("Exceeded Timeout of %s", Duration.ofMillis(timeoutInMillisec)));
            throw e;
        }

        trace(() -> String.format("Remaining time: %s", Duration.ofMillis(timeoutInMillisec - elapsedTimeInMillisec)));
    }
}
