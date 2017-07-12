/*
 * Copyright 2017 The Retrier AUTHORS.
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
package io.github.ravichaturvedi.retrier.utils;


import io.github.ravichaturvedi.retrier.Caller;
import io.github.ravichaturvedi.retrier.Runner;

public class Callers {

    /**
     * Return a caller that returns null from the provided runner.
     * @param runner
     * @return
     */
    public static Caller<?> from(Runner runner) {
        return () -> {
            runner.run();
            return null;
        };
    }
}
