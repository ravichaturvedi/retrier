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

import io.retrier.handler.exception.ExceptionHandler;

import static io.retrier.Retry.on;

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
    void retry(ExceptionHandler handler, Runner runner) throws Exception;

    /**
     * Retry the caller on all exceptions.
     * @param caller
     * @param <T>
     * @return
     * @throws Exception
     */
    default <T> T retry(Caller<T> caller) throws Exception {
        return retry(ON_ALL_EXCEPTION, caller);
    }

    /**
     * Retry the runner on all exceptions.
     * @param runner
     * @throws Exception
     */
    default void retry(Runner runner) throws Exception {
        retry(ON_ALL_EXCEPTION, runner);
    }
}
