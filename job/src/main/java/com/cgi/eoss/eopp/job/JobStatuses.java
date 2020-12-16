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

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * <p>Utility methods for working with {@link Job.Status} values.</p>
 */
public final class JobStatuses {

    private static final Map<Job.Status, Boolean> IS_NOMINAL = ImmutableMap.<Job.Status, Boolean>builderWithExpectedSize(Job.Status.values().length)
            .put(Job.Status.ACCEPTED, true)
            .put(Job.Status.QUEUED, true)
            .put(Job.Status.RUNNING, true)
            .put(Job.Status.COMPLETED, true)
            .put(Job.Status.CANCELLED, true)
            .put(Job.Status.FAILED, false)
            .put(Job.Status.UNKNOWN, false)
            .build();
    private static final Map<Job.Status, Boolean> IS_TERMINAL = ImmutableMap.<Job.Status, Boolean>builderWithExpectedSize(Job.Status.values().length)
            .put(Job.Status.ACCEPTED, false)
            .put(Job.Status.QUEUED, false)
            .put(Job.Status.RUNNING, false)
            .put(Job.Status.COMPLETED, true)
            .put(Job.Status.CANCELLED, true)
            .put(Job.Status.FAILED, true)
            .put(Job.Status.UNKNOWN, false)
            .build();
    private static final Map<Job.Status, Boolean> IS_ACTIVE = ImmutableMap.<Job.Status, Boolean>builderWithExpectedSize(Job.Status.values().length)
            .put(Job.Status.ACCEPTED, false)
            .put(Job.Status.QUEUED, true)
            .put(Job.Status.RUNNING, true)
            .put(Job.Status.COMPLETED, false)
            .put(Job.Status.CANCELLED, false)
            .put(Job.Status.FAILED, false)
            .put(Job.Status.UNKNOWN, false)
            .build();

    private JobStatuses() {
        // no-op for utility class
    }

    /**
     * @return True if the Job.Status signals the job has not entered a failure state, i.e. there has been no step
     * failure or violation of output contracts.
     */
    public static boolean isNominal(Job.Status status) {
        return IS_NOMINAL.get(status);
    }

    /**
     * @return True if the Job.Status signals a terminal state, and will not change again.
     */
    public static boolean isTerminal(Job.Status status) {
        return IS_TERMINAL.get(status);
    }

    /**
     * @return True if the Job.Status signals a job which is currently executing.
     */
    public static boolean isActive(Job.Status status) {
        return IS_ACTIVE.get(status);
    }

}