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


import io.retrier.Handler;

public class RetryCountHandler implements Handler {

  private final int retryCount;

  public RetryCountHandler(int retryCount) {
    if (retryCount < 0) {
      throw new IllegalArgumentException("Retry count should be positive.");
    }

    this.retryCount = retryCount;
  }

  @Override
  public void handleException(Exception e) throws Exception {
    
  }
}
