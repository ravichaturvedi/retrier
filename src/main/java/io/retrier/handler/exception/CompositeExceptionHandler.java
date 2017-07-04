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
package io.retrier.handler.exception;


import io.retrier.handler.AbstractHandler;
import io.retrier.handler.Handler;

public class CompositeExceptionHandler extends AbstractHandler implements ExceptionHandler {

  public CompositeExceptionHandler(Handler... handlers) {
    super(handlers);
  }

  @Override
  public void handleException(Exception e) throws Exception {
    // Try to get the exception handled by all the exception handler and
    // short-circuit on the first successful handling.
    for (Handler handler : getHandlers()) {
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
