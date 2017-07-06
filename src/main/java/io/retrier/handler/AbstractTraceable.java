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
package io.retrier.handler;


import io.retrier.Tracer;

import java.util.function.Supplier;

public abstract class AbstractTraceable implements Traceable {

    protected volatile Tracer tracer;
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
