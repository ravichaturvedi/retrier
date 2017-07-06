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


import io.retrier.handler.exception.CompositeExceptionHandler;
import io.retrier.handler.exception.ExceptionHandler;
import io.retrier.handler.exception.ExceptionRunnerExceptionHandler;
import io.retrier.handler.exception.ExceptionsExceptionHandler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static io.retrier.Retriers.*;

public class Retry {

    private static final Retrier RETRIER = create(
            withRetryCount(3),
            withExpBackoff(Duration.of(1, ChronoUnit.SECONDS)),
            withTimeout(Duration.of(15, ChronoUnit.SECONDS)));

    public static ExceptionHandler on(ExceptionHandler... handlers) {
        return new CompositeExceptionHandler(handlers);
    }

    @SafeVarargs
    public static ExceptionHandler on(Class<? extends Exception>... exceptionClasses) {
        return new ExceptionsExceptionHandler(false, exceptionClasses);
    }

    public static ExceptionHandler on(Class<? extends Exception> exceptionClass, Runner runner) {
        return new ExceptionRunnerExceptionHandler(false, exceptionClass, runner);
    }

    @SafeVarargs
    public static ExceptionHandler onNested(Class<? extends Exception>... exceptionClasses) {
        return new ExceptionsExceptionHandler(true, exceptionClasses);
    }

    public static ExceptionHandler onNested(Class<? extends Exception> exceptionClass, Runner runner) {
        return new ExceptionRunnerExceptionHandler(true, exceptionClass, runner);
    }

    public static <T> T retry(ExceptionHandler handler, Caller<T> caller) throws Exception {
        return RETRIER.retry(handler, caller);
    }

    public static void retry(ExceptionHandler handler, Runner runner) throws Exception {
        RETRIER.retry(handler, runner);
    }

    public static <T> T retry(Caller<T> caller, ExceptionHandler... handlers) throws Exception {
        return RETRIER.retry(caller, handlers);
    }

    public static void retry(Runner runner, ExceptionHandler... handlers) throws Exception {
        RETRIER.retry(runner, handlers);
    }
}
