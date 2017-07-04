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
package io.retrier.handler.exception;


import io.retrier.Runner;
import io.retrier.handler.Handler;
import io.retrier.utils.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ExceptionRunnerExceptionHandler} is a {@link Handler} implementation to run the runner when provided exception occurs.
 */
public class ExceptionRunnerExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionRunnerExceptionHandler.class);

    private final Class<? extends Exception> exceptionClass;
    private final Runner runner;

    public ExceptionRunnerExceptionHandler(Class<? extends Exception> exceptionClass, Runner runner) {
        Preconditions.ensureNotNull(exceptionClass, "Exception class cannot be null.");
        Preconditions.ensureNotNull(runner, "Runner cannot be null.");
        this.exceptionClass = exceptionClass;
        this.runner = runner;
    }

    @Override
    public void handleException(Exception e) throws Exception {
        // If not able to handle the exception then raise it.
        if (!this.exceptionClass.isAssignableFrom(e.getClass())) {
            logFailure(e);
            throw e;
        }

        // Otherwise run the provided runner.
        logSuccess(e);
        this.runner.run();
    }

    private void logFailure(Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Not able to handle the exception: {}", e);
        }
    }

    private void logSuccess(Exception e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("'{}' is handled by Exception Class '{}'.", e, exceptionClass);
        }
    }
}
