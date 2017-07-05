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
import java.util.concurrent.atomic.AtomicInteger;

import static io.retrier.Retriers.*;
import static io.retrier.Retry.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class TestDefaultRetrier {

    @Test
    public void testRetrier() throws Exception {
        Retrier retrier = create(withRetryCount(3),
                withTimeout(Duration.of(5, ChronoUnit.SECONDS)),
                withExpBackoff(Duration.of(4, ChronoUnit.SECONDS), Duration.of(3, ChronoUnit.SECONDS)),
                withTrace(System.out::println));

        AtomicInteger count = new AtomicInteger(0);

        try {
            retrier.retry(on(Exception.class), () -> {
                count.incrementAndGet();
                System.out.println("Hello");
                throw new IllegalArgumentException("123");
            });
            fail("Should have got IllegalArgumentException.");
        } catch (Exception e) {
            assertThat(e.getClass(), is(equalTo(IllegalArgumentException.class)));
            assertThat(e.getMessage(), is("123"));
        }

        assertThat(count.get(), is(2));
    }
}
