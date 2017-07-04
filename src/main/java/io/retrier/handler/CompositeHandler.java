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


import io.retrier.handler.exception.ExceptionHandler;
import io.retrier.handler.limit.CompositeLimitHandler;
import io.retrier.handler.limit.LimitHandler;
import io.retrier.handler.limit.RetryCountLimitHandler;
import io.retrier.handler.limit.TimeoutLimitHandler;
import io.retrier.option.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompositeHandler implements Handler {

    private final List<Handler> handlers;

    public CompositeHandler(Config config, ExceptionHandler exceptionHandler) {
        this.handlers = createHandlers(config, exceptionHandler);
    }

    private List<Handler> createHandlers(Config config, ExceptionHandler exceptionHandler) {
        List<LimitHandler> beforeHandlers = new ArrayList<>(2);
        List<LimitHandler> afterHandlers = new ArrayList<>(2);

        if (config.timeoutDuration != null) {
            LimitHandler timeoutLimitHandler = new TimeoutLimitHandler(config.timeoutDuration.toMillis());
            beforeHandlers.add(timeoutLimitHandler);
            afterHandlers.add(timeoutLimitHandler);
        }

        if (config.maxRetries != null) {
            beforeHandlers.add(new RetryCountLimitHandler(config.maxRetries));
        }

        return Arrays.asList(
                new CompositeLimitHandler(beforeHandlers.toArray(new LimitHandler[0])),
                exceptionHandler,
                new CompositeLimitHandler(afterHandlers.toArray(new LimitHandler[0])));
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
