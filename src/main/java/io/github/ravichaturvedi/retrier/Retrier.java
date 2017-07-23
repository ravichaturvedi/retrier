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

import io.github.ravichaturvedi.retrier.handler.exception.ExceptionHandler;

import static io.github.ravichaturvedi.retrier.Retry.on;

public interface Retrier {

    // Handler dealing with all types of exception as handling `Exception.class`
    ExceptionHandler ON_ALL_EXCEPTION = on(Exception.class);

    /**
     * Retry the caller using the provided exception handler which can be created using `Retry.on` factory methods.
     * @param handler
     * @param caller
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T retry(ExceptionHandler handler, Caller<T> caller) throws Exception;

    /**
     * Retry the runner using the provided exception handler which can be created using `Retry.on` factory methods.
     * @param handler
     * @param runner
     * @throws Exception
     */
    default void retry(ExceptionHandler handler, Runner runner) throws Exception {
        retry(handler, from(runner));
    }

    /**
     * Retry the caller on all exceptions.
     * @param caller
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T retry(Caller<T> caller, ExceptionHandler... handlers) throws Exception {
        switch (handlers.length) {
            case 0:
                return retry(ON_ALL_EXCEPTION, caller);
            case 1:
                return retry(handlers[0], caller);
            default:
                return retry(on(handlers), caller);
        }
    }

    /**
     * Retry the runner on all exceptions.
     * @param runner
     * @throws Exception
     */
    default void retry(Runner runner, ExceptionHandler... handlers) throws Exception {
        retry(from(runner), handlers);
    }

    /**
     * Return a caller that returns null from the provided runner.
     * @param runner
     * @return
     */
    static Caller<?> from(Runner runner) {
        return () -> {
            runner.run();
            return null;
        };
    }
}
