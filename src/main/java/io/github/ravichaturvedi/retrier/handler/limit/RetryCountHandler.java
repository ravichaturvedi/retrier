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
import io.github.ravichaturvedi.retrier.handler.AbstractTraceable;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.ravichaturvedi.retrier.helper.Ensurer.ensure;


/**
 * {@link RetryCountHandler} is a {@link Handler} implementation to make sure retry is happening within the max retries limit.
 */
public class RetryCountHandler extends AbstractTraceable implements Handler {

    // Max number of retrier permitted.
    private final int maxRetries;

    // Keeping track of retry count.
    private final AtomicInteger retryCount;

    public RetryCountHandler(int maxRetries) {
        ensure(maxRetries > 0, "Max retry count should be positive.");
        this.maxRetries = maxRetries;
        this.retryCount = new AtomicInteger();
    }

    @Override
    public void handlePreExec() {
        retryCount.incrementAndGet();
    }

    @Override
    public void handleException(Exception e) throws Exception {
        if (retryCount.get() >= maxRetries) {
            trace(() -> String.format("Exceeded Max Retries: %s", maxRetries));
            throw e;
        }

        trace(() -> String.format("Retry Count: %s/%s", retryCount.get() + 1, maxRetries));
    }
}
