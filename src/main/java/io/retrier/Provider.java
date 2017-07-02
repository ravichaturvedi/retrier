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


/**
 * Provider provides the value after executing the code block wrapped within the lambda expression.
 * @param <V> Type of the returned value.
 */
@FunctionalInterface
public interface Provider<V> {

  /**
   * Provide the value after executing the implementation.
   * @throws Exception If underlying implementation throws.
   */
  V provide() throws Exception;
}
