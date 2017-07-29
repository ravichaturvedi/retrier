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
package io.github.ravichaturvedi.retrier.handler;


import io.github.ravichaturvedi.retrier.Handler;
import io.github.ravichaturvedi.retrier.ThreadLocalTracer;

import java.util.function.Supplier;

/**
 * {@link Traceable} is an abstract class so that individual {@link Handler} can rely on provided trace method.
 */
public abstract class Traceable {

    // Prefix to be put in while tracking (helping in keeping track of which class is getting traced.)
    private final String logPrefix;

    public Traceable() {
        this.logPrefix = String.format("%s: ", this.getClass().getName());
    }

    /**
     * Delegates the trace calls to {@link ThreadLocalTracer}.
     * @param msgSupplier
     */
    protected void trace(Supplier<String> msgSupplier) {
        ThreadLocalTracer.trace(logPrefix + msgSupplier.get());
    }
}
