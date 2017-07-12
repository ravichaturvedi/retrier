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


import io.github.ravichaturvedi.retrier.Tracer;
import io.github.ravichaturvedi.retrier.handler.Handler;
import io.github.ravichaturvedi.retrier.utils.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CompositeExceptionHandler implements ExceptionHandler {

    private final List<ExceptionHandler> handlers;

    public CompositeExceptionHandler(ExceptionHandler... exceptionHandlers) {
        Stream.of(exceptionHandlers).forEach(handler -> Preconditions.ensureNotNull(handler, "ExceptionHandler cannot be null."));
        this.handlers = Collections.unmodifiableList(Arrays.asList(exceptionHandlers));
    }

    @Override
    public void setTracer(Tracer tracer) {
        handlers.forEach(handler -> handler.setTracer(tracer));
    }

    @Override
    public void handleException(Exception e) throws Exception {
        // Try to get the exception handled by all the exception handler and
        // short-circuit on the first successful handling.
        for (Handler handler : handlers) {
            try {
                handler.handleException(e);
                return;
            } catch (Exception ex) {
                // Do nothing
            }
        }

        // If none of the handler handles the exception then propogate it.
        throw e;
    }
}
