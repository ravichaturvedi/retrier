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

import java.util.concurrent.Callable;

import static io.github.ravichaturvedi.retrier.Retry.on;


/**
 * {@link Retrier} is the core interface of the `Retrier` library.
 */
public interface Retrier {

    // Handler dealing with all types of exception as handling `Exception.class`
    Handler ON_ALL_EXCEPTION = on(Exception.class);

    /**
     * Retry the {@link Callable} with the provided {@link Handler}, which can be created using `Retry.on` factory methods.
     * @param handler
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T retry(Handler handler, Callable<T> callable) throws Exception;

    /**
     * Retry the {@link Runner} with the provided {@link Handler}, which can be created using `Retry.on` factory methods.
     * @param handler
     * @param runner
     * @throws Exception
     */
    default void retry(Handler handler, Runner runner) throws Exception {
        retry(handler, from(runner));
    }

    /**
     * Retry the {@link Callable} with the provided {@link Handler}, which can be created using `Retry.on` factory methods.
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T retry(Callable<T> callable, Handler handler) throws Exception {
        return retry(handler, callable);
    }

    /**
     * Retry the {@link Runner} with the provided {@link Handler}, which can be created using `Retry.on` factory methods.
     * @param runner
     * @throws Exception
     */
    default void retry(Runner runner, Handler handler) throws Exception {
        retry(from(runner), handler);
    }

    /**
     * Retry the {@link Callable} on all {@link Exception}s.
     * @param callable
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T retry(Callable<T> callable) throws Exception {
        return retry(ON_ALL_EXCEPTION, callable);
    }

    /**
     * Retry the {@link Runner} on all {@link Exception}s.
     * @param runner
     * @throws Exception
     */
    default void retry(Runner runner) throws Exception {
        retry(from(runner));
    }

    /**
     * Return a {@link Callable} that returns null from the provided {@link Runner}.
     * @param runner
     * @return
     */
    static Callable<?> from(Runner runner) {
        return () -> {
            runner.run();
            return null;
        };
    }
}
