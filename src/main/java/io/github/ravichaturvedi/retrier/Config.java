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
 * {@link Config} defines the various config that can be supplied to the Retrier.
 */
public class Config {
    // Max Number of Retries
    public Integer maxRetries;

    // Timeout Duration
    public Duration timeoutDuration;

    // Exponential Backoff Duration
    public Duration expBackoffDuration;

    // Max Exponential Backoff Duration
    public Duration expBackoffMaxDuration;

    // Tracer to trace the execution
    public Tracer tracer;

    /**
     * Returns a new copy of the {@link Config}.
     *
     * @return
     */
    public Config copy() {
        Config config = new Config();
        config.expBackoffDuration = expBackoffDuration;
        config.maxRetries = maxRetries;
        config.timeoutDuration = timeoutDuration;
        config.expBackoffMaxDuration = expBackoffMaxDuration;
        config.tracer = tracer;
        return config;
    }

    @Override
    public String toString() {
        return "Config{" +
                "maxRetries=" + maxRetries +
                ", timeoutDuration=" + timeoutDuration +
                ", expBackoffDuration=" + expBackoffDuration +
                ", expBackoffMaxDuration=" + expBackoffMaxDuration +
                ", tracer=" + tracer +
                '}';
    }
}
