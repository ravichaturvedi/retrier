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


import org.junit.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static io.retrier.Retriers.*;
import static io.retrier.Retry.*;

public class TestDefaultRetrier {

    @Test
    public void testRetrier() throws Exception {
        Retrier retrier = create(withRetryCount(3),
                withTimeout(Duration.of(15, ChronoUnit.SECONDS)),
                withExpBackoffDelay(Duration.of(2, ChronoUnit.SECONDS)),
                withLogger(System.out::println));

        retrier.retry(on(Exception.class), () -> {
            System.out.println("Hello");
            throw new IllegalArgumentException("123");
        });
    }
}
