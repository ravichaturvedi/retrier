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
package io.github.ravichaturvedi.retrier;


/**
 * {@link ThreadLocalTracer} is keeping track of thread specific tracers and have trace method to delegate the call to {@link Thread} specific {@link Tracer}.
 */
public class ThreadLocalTracer {

    /**
     * Keeping track of tracer on the per {@link Thread} basis.
     */
    private static ThreadLocal<Tracer> tracerThreadLocal = new ThreadLocal<>();

    /**
     * Associate the {@link Tracer} with the current {@link Thread}.
     * @param tracer
     */
    public static void setTracer(Tracer tracer) {
        tracerThreadLocal.set(tracer);
    }

    /**
     * Remove the {@link Tracer} associated with the current {@link Thread}.
     */
    public static void removeTracer() {
        tracerThreadLocal.remove();
    }

    /**
     * Trace the given msg using the {@link ThreadLocal<Tracer>}
     * @param msg
     */
    public static void trace(String msg) {
        Tracer tracer = tracerThreadLocal.get();
        if (tracer != null) {
            tracer.trace(msg);
        }
    }
}
