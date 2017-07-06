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
package io.retrier.handler.exception;

import io.retrier.utils.Exceptions;
import io.retrier.Runner;
import io.retrier.handler.AbstractTraceable;
import io.retrier.handler.Handler;
import io.retrier.utils.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * {@link ExceptionRunnerExceptionHandler} is a {@link Handler} implementation to run the runner when provided exception occurs.
 */
public class ExceptionRunnerExceptionHandler extends AbstractTraceable implements ExceptionHandler {

    private final Class<? extends Exception> exceptionClass;
    private final Runner runner;
    private final boolean nested;

    public ExceptionRunnerExceptionHandler(boolean nested, Class<? extends Exception> exceptionClass, Runner runner) {
        Preconditions.ensureNotNull(exceptionClass, "Exception class cannot be null.");
        Preconditions.ensureNotNull(runner, "Runner cannot be null.");
        this.exceptionClass = exceptionClass;
        this.runner = runner;
        this.nested = nested;
    }

    @Override
    public void handleException(Exception e) throws Exception {
        List<Class<? extends Exception>> exceptionClasses = nested ? Exceptions.getNestedExceptionClasses(e): Collections.singletonList(e.getClass());

        // If not able to handle the exception then raise it.
        if (!exceptionClasses.stream().map(this.exceptionClass::isAssignableFrom).findAny().isPresent()) {
            trace(() -> String.format("Unknown Exception: %s", e));
            throw e;
        }

        // Otherwise run the provided runner.
        trace(() -> String.format("Caught Exception: '%s' by '%s'", e, exceptionClass));
        this.runner.run();
    }
}
