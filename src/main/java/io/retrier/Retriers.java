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
package io.retrier;


import io.retrier.option.Config;
import io.retrier.option.Option;
import io.retrier.option.Options;

import java.time.Duration;

public class Retriers {

    public static Retrier create(Option... opts) {
        Config config = new Config();
        new Options(opts).process(config);
        return new DefaultRetrier(config.copy());
    }

    public static Option withRetryCount(int retryCount) {
        return c -> c.maxRetries = retryCount;
    }

    public static Option withTimeout(Duration duration) {
        return c -> c.timeoutDuration = duration;
    }

    public static Option withExpBackoff(Duration delay) {
        return c -> c.expBackoffDuration = delay;
    }

    public static Option withExpBackoff(Duration delay, Duration maxDelay) {
        return c -> {
            withExpBackoff(delay);
            c.expBackoffMaxDuration = maxDelay;
        };
    }

    public static Option withTrace(Logger logger) {
        return c -> c.logger = logger;
    }
}
