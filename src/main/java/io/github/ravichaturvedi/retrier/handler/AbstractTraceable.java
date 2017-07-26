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


import io.github.ravichaturvedi.retrier.Tracer;

import java.util.function.Supplier;

/**
 * {@link AbstractTraceable} is an abstract implementation of {@link Traceable} so that individual handler need can rely on provided trace method.
 */
public abstract class AbstractTraceable implements Traceable {

    /**
     * Tracer to be used for tracing.
     */
    protected volatile Tracer tracer;

    // Prefix to be put in while tracking (helping in keeping track of which class is getting traced.)
    private final String logPrefix;

    public AbstractTraceable() {
        this.logPrefix = String.format("%s: ", this.getClass().getName());
    }

    @Override
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    protected void trace(Supplier<String> msgSupplier) {
        if (this.tracer != null) {
            this.tracer.trace(logPrefix + msgSupplier.get());
        }
    }
}
