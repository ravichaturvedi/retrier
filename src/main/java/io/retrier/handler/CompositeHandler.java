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
import io.retrier.handler.catcher.CatchHandler;
import io.retrier.handler.checker.CheckHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompositeHandler implements Handler {

  private List<Handler> handlers;

  public CompositeHandler(CheckHandler checkHandler, CatchHandler catchHandler) {
    Preconditions.ensureNotNull(checkHandler, "CheckHandler cannot be null.");
    Preconditions.ensureNotNull(catchHandler, "CatchHandler cannot be null.");
    this.handlers = Collections.unmodifiableList(Arrays.asList(checkHandler, catchHandler));
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
    for (Handler handler : handlers) {
      handler.handleException(e);
    }
  }
}
