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
package io.retrier.handler.checker;

import io.retrier.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;


/**
 * {@link TimeoutCheckHandler} is a {@link CheckHandler} implementation to make sure retry is happening within the max timeout provided.
 */
public class TimeoutCheckHandler implements CheckHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutCheckHandler.class);

  private final long timeoutInMillisec;
  private final AtomicLong startTimeInMillisec;

  public TimeoutCheckHandler(long timeoutInMillisec) {
    Preconditions.ensure(timeoutInMillisec > 0, "Timeout should be positive.");
    this.timeoutInMillisec = timeoutInMillisec;
    this.startTimeInMillisec = new AtomicLong(0);
  }

  @Override
  public void handlePreExec() {
    startTimeInMillisec.compareAndSet(0, System.currentTimeMillis());
  }

  @Override
  public void handleException(Exception e) throws Exception {
    if (System.currentTimeMillis() - startTimeInMillisec.get() > timeoutInMillisec) {
      logFailure();
      throw e;
    }

    logSuccess();
  }

  private void logFailure() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Exceeded Timeout (in millisec): {}", timeoutInMillisec);
    }
  }

  private void logSuccess() {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Still have {}ms to retry.", System.currentTimeMillis() - startTimeInMillisec.get());
    }
  }
}
