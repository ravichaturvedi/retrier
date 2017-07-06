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
package io.retrier.utils;


import io.retrier.Caller;
import io.retrier.Runner;

import java.util.ArrayList;
import java.util.List;

public class Exceptions {

    /**
     * Wrap the exception thrown by the provided runner into the runtime exception
     * or keep the same runtime exception if thrown.
     *
     * @param runner
     */
    public static void wrap(Runner runner) {
        try {
            runner.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrap the exception thrown by the provided caller into the runtime exception
     * or keep the same runtime exception if thrown.
     *
     * @param caller
     * @param <V>
     * @return
     */
    public static <V> V wrap(Caller<V> caller) {
        try {
            return caller.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the classes of all nested exception classes along with provided exception class,
     * wrapped within the provided exception.
     *
     * @param e
     * @return
     */
    public static List<Class<? extends Exception>> getNestedExceptionClasses(Exception e) {
        List<Class<? extends Exception>> exceptionClasses = new ArrayList<>();

        while (true) {
            // If the exception is null then return the set.
            if (e == null) {
                return exceptionClasses;
            }

            // Add the class of the provided exception.
            exceptionClasses.add(e.getClass());

            // If the cause is an instance of Exception then only add it to the list, otherwise set the exception to null;
            if (e.getCause() instanceof Exception) {
                e = (Exception)e.getCause();
            } else {
                e = null;
            }
        }
    }
}
