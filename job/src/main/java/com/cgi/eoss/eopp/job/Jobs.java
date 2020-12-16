/*
 * Copyright 2020 The libeopp Team
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

package com.cgi.eoss.eopp.job;

import java.util.NoSuchElementException;

/**
 * <p>Utility methods for working with {@link Job} objects.</p>
 */
public final class Jobs {

    private Jobs() {
        // no-op for utility class
    }

    /**
     * @return The StepInstance with the given identifier in the given Job.
     * @throws NoSuchElementException If the Job contains no StepInstance with the given identifier.
     */
    public static StepInstance getStepInstance(Job job, String stepIdentifier) {
        return job.getStepList().stream().filter(it -> it.getIdentifier().equals(stepIdentifier)).findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Job %s does not contain a step instance with identifier %s", job.getUuid(), stepIdentifier)));
    }

}
