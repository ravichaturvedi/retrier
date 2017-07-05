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


import io.retrier.handler.AbstractLoggable;
import io.retrier.handler.Handler;
import io.retrier.utils.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * {@link ExceptionsExceptionHandler} is a {@link Handler} implementation to catch the provided exceptions while retrying.
 * <p>
 * It is similar to mentioning exceptions in catch block and consuming it.
 * So if the exception raised during retry is subclass of any of the provided exception than it will be consumed and retry will happen again.
 */
public class ExceptionsExceptionHandler extends AbstractLoggable implements ExceptionHandler {

    // Exception classes to be handled.
    private final List<Class<? extends Exception>> exceptionClasses;

    public ExceptionsExceptionHandler(Class<? extends Exception>... exceptionClasses) {
        Stream.of(exceptionClasses).forEach(cls -> Preconditions.ensureNotNull(cls, "Exception class cannot be null."));
        this.exceptionClasses = Collections.unmodifiableList(Arrays.asList(exceptionClasses));
    }

    @Override
    public void handleException(Exception e) throws Exception {
        Optional<Class<? extends Exception>> classHandlingException = exceptionClasses.stream()
                .filter(cls -> cls.isAssignableFrom(e.getClass()))
                .findAny();

        // Raise the incoming exception, if the exception cannot be handled by any of the exception classes provided in constructor.
        if (!classHandlingException.isPresent()) {
            log(() -> String.format("Unknown Exception: %s", e));
            throw e;
        }

        log(() -> String.format("Caught Exception: '%s' by '%s'", e, classHandlingException.get()));
    }
}
