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


import io.retrier.handler.limit.CompositeLimitHandler;
import io.retrier.handler.limit.LimitHandler;
import io.retrier.handler.limit.LimitHandlerProvider;
import io.retrier.handler.limit.RetryCountLimitHandler;
import io.retrier.handler.limit.TimeoutLimitHandler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Retriers {

  public static Retrier create(LimitHandlerProvider firstLimitHandlerProvider, LimitHandlerProvider... otherLimitHandlerProviders) {
    List<LimitHandler> limitHandlers = Stream.concat(Stream.of(firstLimitHandlerProvider), Stream.of(otherLimitHandlerProviders))
        .map(LimitHandlerProvider::provide)
        .collect(Collectors.toList());
    return new DefaultRetrier(() -> new CompositeLimitHandler(limitHandlers.toArray(new LimitHandler[0])));
  }

  public static LimitHandlerProvider withRetryCount(int retryCount) {
    return () -> new RetryCountLimitHandler(retryCount);
  }

  public static LimitHandlerProvider withTimeout(Duration duration) {
    return () -> new TimeoutLimitHandler(duration.toMillis());
  }
}
