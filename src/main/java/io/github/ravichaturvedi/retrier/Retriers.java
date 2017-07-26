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
package io.github.ravichaturvedi.retrier;

import java.time.Duration;

/**
 * {@link Retriers} provide factory for creating new {@link Retrier} and methods to generate {@link Option} for the factory.
 */
public class Retriers {

    /**
     * Create the {@link Retrier} with the provided options.
     * @param opts
     * @return
     */
    public static Retrier create(Option... opts) {
        Config config = new Config();
        new Options(opts).process(config);
        return new DefaultRetrier(config.copy());
    }

    /**
     * Create an {@link Option} with the provided retry count.
     * @param retryCount
     * @return
     */
    public static Option withRetryCount(int retryCount) {
        return c -> c.maxRetries = retryCount;
    }

    /**
     * Create an {@link Option} with the provided timeout duration.
     * @param duration
     * @return
     */
    public static Option withTimeout(Duration duration) {
        return c -> c.timeoutDuration = duration;
    }

    /**
     * Create an {@link Option} with the provided exponential backoff delay.
     * @param delay
     * @return
     */
    public static Option withExpBackoff(Duration delay) {
        return c -> c.expBackoffDuration = delay;
    }

    /**
     * Create an {@link Option} with the provided exponential backoff delay and the max delay.
     * @param delay
     * @param maxDelay
     * @return
     */
    public static Option withExpBackoff(Duration delay, Duration maxDelay) {
        return c -> {
            withExpBackoff(delay).process(c);
            c.expBackoffMaxDuration = maxDelay;
        };
    }

    /**
     * Create an {@link Option} with the provided {@link Tracer} to trace the retry execution.
     * @param tracer
     * @return
     */
    public static Option withTrace(Tracer tracer) {
        return c -> c.tracer = tracer;
    }
}
