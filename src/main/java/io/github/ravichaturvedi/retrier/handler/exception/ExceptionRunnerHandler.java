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
package io.github.ravichaturvedi.retrier.handler.exception;

import io.github.ravichaturvedi.retrier.Runner;
import io.github.ravichaturvedi.retrier.handler.AbstractTraceable;
import io.github.ravichaturvedi.retrier.Handler;

import java.util.List;

import static java.util.Collections.singletonList;
import static io.github.ravichaturvedi.retrier.helper.Ensurer.ensureNotNull;
import static io.github.ravichaturvedi.retrier.helper.Exceptions.getNestedExceptionClasses;

/**
 * {@link ExceptionRunnerHandler} is a {@link Handler} implementation to run the {@link Runner} when provided exception occurs.
 */
public class ExceptionRunnerHandler extends AbstractTraceable implements Handler {

    private final Class<? extends Exception> exceptionClass;
    private final Runner runner;
    private final boolean nested;

    public ExceptionRunnerHandler(boolean nested, Class<? extends Exception> exceptionClass, Runner runner) {
        ensureNotNull(exceptionClass, "Exception class cannot be null.");
        ensureNotNull(runner, "Runner cannot be null.");
        this.exceptionClass = exceptionClass;
        this.runner = runner;
        this.nested = nested;
    }

    @Override
    public void handleException(Exception e) throws Exception {
        List<Class<? extends Exception>> exceptionClasses = nested ? getNestedExceptionClasses(e): singletonList(e.getClass());

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
