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


import io.retrier.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


public abstract class AbstractHandler implements Handler {

    private final List<Handler> handlers;

    public AbstractHandler(Handler... handlers) {
        Stream.of(handlers).forEach(handler -> Preconditions.ensureNotNull(handler, "Handler cannot be null."));
        this.handlers = Collections.unmodifiableList(Arrays.asList(handlers));
    }

    protected List<Handler> getHandlers() {
        return handlers;
    }

    @Override
    public void handlePreExec() {
        handlers.forEach(Handler::handlePreExec);
    }

    @Override
    public <T> T handlePostExec(T result) {
        // Respond back from the first handler which transforms the result
        for (Handler handler : handlers) {
            T resp = handler.handlePostExec(result);
            if (resp != result) {
                return resp;
            }
        }

        // Otherwise return back the result itself.
        return result;
    }
}
