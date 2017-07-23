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
package io.github.ravichaturvedi.retrier;

import io.github.ravichaturvedi.retrier.handler.exception.ExceptionHandler;
import io.github.ravichaturvedi.retrier.handler.CompositeHandler;
import io.github.ravichaturvedi.retrier.handler.Handler;
import io.github.ravichaturvedi.retrier.option.Config;

import static io.github.ravichaturvedi.retrier.helper.Ensurer.ensureNotNull;


class DefaultRetrier implements Retrier {

    private final Config config;

    DefaultRetrier(Config config) {
        ensureNotNull(config, "Config cannot be null.");
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
}
