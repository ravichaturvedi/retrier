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
import io.retrier.handler.limit.LimitHandler;


public class CompositeHandler extends AbstractHandler {

  public CompositeHandler(LimitHandler limitHandler, ExceptionHandler exceptionHandler) {
    super(limitHandler, exceptionHandler);
  }

  @Override
  public void handleException(Exception e) throws Exception {
    // Make sure all the handler checks are successful.
    for (Handler handler : getHandlers()) {
      handler.handleException(e);
    }
  }
}
