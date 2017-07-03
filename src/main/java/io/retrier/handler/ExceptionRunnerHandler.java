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
package io.retrier.handler;


import io.retrier.Handler;
import io.retrier.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionRunnerHandler provides a handler implementation to run the runner when some exception occurs.
 */
public class ExceptionRunnerHandler implements Handler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionRunnerHandler.class);

  private final Class<Exception> exceptionClass;
  private final Runner runner;

  public ExceptionRunnerHandler(Class<Exception> exceptionClass, Runner runner) {
    validate(exceptionClass, runner);
    this.exceptionClass = exceptionClass;
    this.runner = runner;
  }

  private void validate(Class<Exception> exceptionClass, Runner runner) {
    if (exceptionClass == null) {
      throw new IllegalArgumentException("Exception class cannot be null.");
    }

    if (runner == null) {
      throw new IllegalArgumentException("Runner cannot be null.");
    }
  }

  @Override
  public void handleException(Exception e) throws Exception {
    // If not able to handle the exception then raise it.
    if (!e.getClass().isAssignableFrom(this.exceptionClass)) {
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
