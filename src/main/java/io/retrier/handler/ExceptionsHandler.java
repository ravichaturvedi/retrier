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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * {@link ExceptionsHandler} is a {@link Handler} implementation to catch the provided exceptions while retrying.
 *
 * It is similar to mentioning exceptions in catch block and consuming it.
 * So if the exception raised during retry is subclass of any of the provided exception than it will be consumed and retry will happen again.
 */
public class ExceptionsHandler implements Handler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsHandler.class);

  // Exception classes to be handled.
  private final List<Class<Exception>> exceptionClasses;

  public ExceptionsHandler(Class<Exception>... exceptionClasses) {
    validate(exceptionClasses);
    this.exceptionClasses = Collections.unmodifiableList(Arrays.asList(exceptionClasses));
  }

  private void validate(Class<Exception>... exceptionClasses) {
    Arrays.asList(exceptionClasses).forEach(cls -> {
      if (cls == null) {
        throw new IllegalArgumentException("Exception class cannot be null.");
      }
    });
  }

  @Override
  public void handleException(Exception e) throws Exception {
    Optional<Class<Exception>> classHandlingException = exceptionClasses.stream()
        .filter(cls -> e.getClass().isAssignableFrom(cls))
        .findAny();

    // Raise the incoming exception, if the exception cannot be handled by any of the exception classes provided in constructor.
    if (!classHandlingException.isPresent()) {
      logFailure(e);
      throw e;
    }

    logSuccess(e, classHandlingException.get());
  }

  private void logFailure(Exception e) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Not able to handle the exception: {}", e);
    }
  }

  private void logSuccess(Exception e, Class<Exception> exceptionClass) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("'{}' is handled by Exception Class '{}'.", e, exceptionClass);
    }
  }
}
