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
package io.retrier.handler.catcher;


import io.retrier.handler.Handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompositeCatchHandler implements CatchHandler {

  private final List<Handler> handlers;

  public CompositeCatchHandler(Handler... handlers) {
    this.handlers = Collections.unmodifiableList(Arrays.asList(handlers));
  }

  @Override
  public void handleException(Exception e) throws Exception {
    // Try to get the exception handled by all the retry statichandler and short-circuit on the first successful handling.
    for (Handler handler : handlers) {
      try {
        handler.handleException(e);
        return;
      } catch (Exception ex) {
        // If exception instance is not same as passed to handleException then raise it.
        if (ex != e) {
          throw ex;
        }
      }
    }

    // If none of the retry handle handles the exception then propogate it.
    throw e;
  }
}
