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
package io.github.ravichaturvedi.retrier.handler;


import io.github.ravichaturvedi.retrier.Config;
import io.github.ravichaturvedi.retrier.Handler;
import io.github.ravichaturvedi.retrier.handler.limit.ExpBackoffHandler;
import io.github.ravichaturvedi.retrier.handler.limit.RetryCountHandler;
import io.github.ravichaturvedi.retrier.handler.limit.TimeoutHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link CompositeHandler} is a {@link Handler} implementation which chains both Limit and Composite handlers.
 */
public class CompositeHandler implements Handler {

    // Chained handlers
    private final List<Handler> handlers;

    public CompositeHandler(Config config, Handler handler) {
        this.handlers = createHandlers(config, handler);
    }

    /**
     * Creates the handler chain from the provided {@link Config} (which creates limit) and exception {@link Handler}
     * @param config
     * @param handler
     * @return
     */
    private List<Handler> createHandlers(Config config, Handler handler) {
        List<Handler> beforeHandlers = new ArrayList<>(2);
        List<Handler> afterHandlers = new ArrayList<>(2);

        if (config.timeoutDuration != null) {
            Handler timeoutHandler = new TimeoutHandler(config.timeoutDuration.toMillis());
            beforeHandlers.add(timeoutHandler);
            afterHandlers.add(timeoutHandler);
        }

        if (config.maxRetries != null) {
            beforeHandlers.add(new RetryCountHandler(config.maxRetries));
        }

        if (config.expBackoffDuration != null && config.expBackoffMaxDuration != null) {
            beforeHandlers.add(new ExpBackoffHandler(config.expBackoffDuration.toMillis(), config.expBackoffMaxDuration.toMillis()));
        }

        if (config.expBackoffDuration != null && config.expBackoffMaxDuration == null) {
            beforeHandlers.add(new ExpBackoffHandler(config.expBackoffDuration.toMillis()));
        }

        return Arrays.asList(
                new io.github.ravichaturvedi.retrier.handler.limit.CompositeHandler(beforeHandlers.toArray(new Handler[0])),
                handler,
                new io.github.ravichaturvedi.retrier.handler.limit.CompositeHandler(afterHandlers.toArray(new Handler[0])));
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

    @Override
    public void handleException(Exception e) throws Exception {
        // Make sure all the handler checks are successful.
        for (Handler handler : handlers) {
            handler.handleException(e);
        }
    }
}
