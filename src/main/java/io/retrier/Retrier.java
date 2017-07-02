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

public class Retrier {

  <T> T retry(Handler handler, Provider<T> provider) throws Exception {
    while (true) {
      try {
        handler.handlePreExec();
        T result = provider.provide();
        handler.handlePostExec(result);
        return result;
      } catch (Exception e) {
        handler.handleException(e);
      }
    }
  }

  void retry(Handler handler, Runner runner) throws Exception {
    retry(handler, () -> {
      runner.run();
      return null;
    });
  }
}
