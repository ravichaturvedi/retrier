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


import io.github.ravichaturvedi.retrier.Handler;

import java.util.List;
import java.util.stream.Stream;

import static io.github.ravichaturvedi.retrier.helper.Ensurer.ensureNotNull;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;


/**
 * {@link CompositeHandler} is a {@link Handler} implementation to aggregate the exception Handlers.
 */
public class CompositeHandler implements Handler {

    // List of Exception Handlers
    private final List<Handler> handlers;

    public CompositeHandler(Handler... handlers) {
        Stream.of(handlers).forEach(handler -> ensureNotNull(handler, "Handler cannot be null."));
        this.handlers = unmodifiableList(asList(handlers));
    }

    @Override
    public void handleException(Exception e) throws Exception {
        // Try to get the exception handled by all the exception handler and
        // short-circuit on the first successful handling, to provide the same semantics as try-catch-catch blocks.
        for (Handler handler : handlers) {
            try {
                handler.handleException(e);
                return;
            } catch (Exception ex) {
                // Do nothing, as we don't want to propagate the exception thrown by the Handlers.
            }
        }

        // If none of the handler handles the exception then propagate it.
        throw e;
    }
}
