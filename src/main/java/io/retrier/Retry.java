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


import io.retrier.handler.catcher.CompositeCatchHandler;
import io.retrier.handler.catcher.ExceptionRunnerCatchHandler;
import io.retrier.handler.catcher.ExceptionsCatchHandler;
import io.retrier.handler.catcher.CatchHandler;

public class Retry {

  public static CatchHandler on(CatchHandler... handlers) {
    return new CompositeCatchHandler(handlers);
  }

  public static CatchHandler on(Class<Exception>... exceptionClasses) {
    return new ExceptionsCatchHandler(exceptionClasses);
  }

  public static CatchHandler on(Class<Exception> exceptionClass, Runner runner) {
    return new ExceptionRunnerCatchHandler(exceptionClass, runner);
  }
}
