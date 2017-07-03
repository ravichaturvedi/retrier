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
import io.retrier.handler.catcher.CatchHandler;
import io.retrier.handler.checker.CheckHandlerProvider;

public class DefaultRetrier implements Retrier {

  private final CheckHandlerProvider checkHandlerProvider;

  public DefaultRetrier(CheckHandlerProvider checkHandlerProvider) {
    Preconditions.ensureNotNull(checkHandlerProvider, "CheckHandlerProvider cannot be null.");
    this.checkHandlerProvider = checkHandlerProvider;
  }

  public <T> T retry(CatchHandler catchHandler, Provider<T> provider) throws Exception {
    // Create the consolidated handler to fire the events on.
    Handler handler = new CompositeHandler(checkHandlerProvider.provide(), catchHandler);

    while (true) {
      try {
        handler.handlePreExec();
        T result = provider.provide();
        result = handler.handlePostExec(result);
        return result;
      } catch (Exception e) {
        handler.handleException(e);
      }
    }
  }

  public void retry(CatchHandler handler, Runner runner) throws Exception {
    retry(handler, () -> {
      runner.run();
      return null;
    });
  }
}
