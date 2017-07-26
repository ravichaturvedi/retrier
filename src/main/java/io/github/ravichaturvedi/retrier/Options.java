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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link Options} is an {@link Option} which aggregate and process the provided {@link Option}s in order.
 */
class Options implements Option {

    // List of Option to be processed.
    private final List<Option> opts;

    public Options(Option... opts) {
        this.opts = Collections.unmodifiableList(Arrays.asList(opts));
    }

    @Override
    public void process(Config config) {
        opts.forEach(opt -> opt.process(config));
    }
}
