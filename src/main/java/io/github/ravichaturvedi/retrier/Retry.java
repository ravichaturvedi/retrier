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


import io.github.ravichaturvedi.retrier.handler.exception.CompositeHandler;
import io.github.ravichaturvedi.retrier.handler.exception.ExceptionRunnerHandler;
import io.github.ravichaturvedi.retrier.handler.exception.ExceptionsHandler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;

import static io.github.ravichaturvedi.retrier.Retriers.*;

/**
 * {@link Retry} provides methods for default {@link Retrier} and `on` methods to create {@link Handler}s.
 */
public class Retry {

    /**
     * Default {@link Retrier} with retry count 3, exponential backoff duration of 1 second and timout of 15 seconds.
     */
    private static final Retrier RETRIER = create(
            withRetryCount(3),
            withExpBackoff(Duration.of(1, ChronoUnit.SECONDS)),
            withTimeout(Duration.of(15, ChronoUnit.SECONDS)));

    /**
     * Retry the  {@link Callable} with the provided {@link Handler}, using the default {@link Retrier}.
     * @param handler
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T retry(Handler handler, Callable<T> callable) throws Exception {
        return RETRIER.retry(handler, callable);
    }

    /**
     * Retry the {@link Runner} with the provided Handler, using the default {@link Retrier}.
     * @param handler
     * @param runner
     * @throws Exception
     */
    public static void retry(Handler handler, Runner runner) throws Exception {
        RETRIER.retry(handler, runner);
    }

    /**
     * Retry the {@link Callable} with the provided {@link Handler}, using the default {@link Retrier}.
     * @param callable
     * @param handler
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T retry(Callable<T> callable, Handler handler) throws Exception {
        return RETRIER.retry(callable, handler);
    }

    /**
     * Retry the {@link Runner} with the provided {@link Handler}, using the default {@link Retrier}.
     * @param runner
     * @param handler
     * @throws Exception
     */
    public static void retry(Runner runner, Handler handler) throws Exception {
        RETRIER.retry(runner, handler);
    }

    /**
     * Retry the {@link Callable} on all {@link Exception}s, using the default {@link Retrier}.
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T retry(Callable<T> callable) throws Exception {
        return RETRIER.retry(callable);
    }

    /**
     * Retry the {@link Runner} on all {@link Exception}s, using the default {@link Retrier}.
     * @param runner
     * @throws Exception
     */
    public static void retry(Runner runner) throws Exception {
        RETRIER.retry(runner);
    }

    /**
     * Create a handler to aggregate and handle using the provided handler.
     * It is generally used when need to pass multiple handler in first argument of retry method.
     * @param handlers
     * @return
     */
    public static Handler on(Handler... handlers) {
        return new CompositeHandler(handlers);
    }

    /**
     * Returns a {@link Handler} to handle the provided {@link Exception} classes.
     * @param exceptionClasses
     * @return
     */
    @SafeVarargs
    public static Handler on(Class<? extends Exception>... exceptionClasses) {
        return new ExceptionsHandler(false, exceptionClasses);
    }

    /**
     * Returns a {@link Handler} to run the {@link Runner} when provided {@link Exception} occurs.
     * @param exceptionClass
     * @param runner
     * @return
     */
    public static Handler on(Class<? extends Exception> exceptionClass, Runner runner) {
        return new ExceptionRunnerHandler(false, exceptionClass, runner);
    }

    /**
     * Returns a {@link Handler} to handle the provided {@link Exception} classes when the thrown or nested exception (provided by getCause()) has that type.
     * @param exceptionClasses
     * @return
     */
    @SafeVarargs
    public static Handler onNested(Class<? extends Exception>... exceptionClasses) {
        return new ExceptionsHandler(true, exceptionClasses);
    }

    /**
     * Returns a {@link Handler} to run the {@link Runner} when provided {@link Exception} occurs or its nested {@link Exception} (provided by getCause()) has that type/
     * @param exceptionClass
     * @param runner
     * @return
     */
    public static Handler onNested(Class<? extends Exception> exceptionClass, Runner runner) {
        return new ExceptionRunnerHandler(true, exceptionClass, runner);
    }
}
