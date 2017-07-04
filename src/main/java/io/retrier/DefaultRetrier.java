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
package io.retrier;

import io.retrier.handler.CompositeHandler;
import io.retrier.handler.Handler;
import io.retrier.handler.exception.ExceptionHandler;
import io.retrier.option.Config;
import io.retrier.utils.Preconditions;


class DefaultRetrier implements Retrier {

    private final Config config;

    DefaultRetrier(Config config) {
        Preconditions.ensureNotNull(config, "Config cannot be null.");
        this.config = config;
    }

    @Override
    public <T> T retry(ExceptionHandler exceptionHandler, Caller<T> caller) throws Exception {
        Handler handler = new CompositeHandler(config, exceptionHandler);

        while (true) {
            try {
                handler.handlePreExec();
                T result = caller.call();
                return handler.handlePostExec(result);
            } catch (Exception e) {
                handler.handleException(e);
            }
        }
    }

    @Override
    public void retry(ExceptionHandler handler, Runner runner) throws Exception {
        retry(handler, () -> {
            runner.run();
            return null;
        });
    }
}
