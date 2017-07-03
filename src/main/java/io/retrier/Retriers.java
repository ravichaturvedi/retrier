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


import io.retrier.handler.checker.CheckHandler;
import io.retrier.handler.checker.CheckHandlerProvider;
import io.retrier.handler.checker.CompositeCheckHandler;
import io.retrier.handler.checker.RetryCountCheckHandler;
import io.retrier.handler.checker.TimeoutCheckHandler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Retriers {

  public static Retrier create(CheckHandlerProvider firstCheckHandlerProvider, CheckHandlerProvider... otherCheckHandlerProviders) {
    List<CheckHandler> checkHandlers = Stream.concat(Stream.of(firstCheckHandlerProvider), Stream.of(otherCheckHandlerProviders))
        .map(CheckHandlerProvider::provide)
        .collect(Collectors.toList());
    return new DefaultRetrier(() -> new CompositeCheckHandler(checkHandlers.toArray(new CheckHandler[0])));
  }

  public static CheckHandlerProvider withRetryCount(int retryCount) {
    return () -> new RetryCountCheckHandler(retryCount);
  }

  public static CheckHandlerProvider withTimeout(Duration duration) {
    return () -> new TimeoutCheckHandler(duration.get(ChronoUnit.MILLIS));
  }
}
