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

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>Simplified job statuses for a 'traffic light' display.</p>
 */
public enum ShortJobStatus {
    /**
     * <p>The Job is in a non-terminal status.</p>
     */
    IN_PROGRESS,
    /**
     * <p>The Job terminated in a nominal way.</p>
     */
    SUCCEEDED,
    /**
     * <p>The Job terminated in a non-nominal way.</p>
     */
    FAILED;

    private static final Map<Job.Status, ShortJobStatus> SHORT_STATUSES = ImmutableMap.<Job.Status, ShortJobStatus>builderWithExpectedSize(Job.Status.values().length)
            .put(Job.Status.ACCEPTED, ShortJobStatus.IN_PROGRESS)
            .put(Job.Status.QUEUED, ShortJobStatus.IN_PROGRESS)
            .put(Job.Status.RUNNING, ShortJobStatus.IN_PROGRESS)
            .put(Job.Status.COMPLETED, ShortJobStatus.SUCCEEDED)
            .put(Job.Status.CANCELLED, ShortJobStatus.SUCCEEDED)
            .put(Job.Status.FAILED, ShortJobStatus.FAILED)
            .put(Job.Status.UNKNOWN, ShortJobStatus.IN_PROGRESS)
            .build();

    /**
     * @return The counts of each ShortJobStatus in the given Job.Status collection.
     */
    public static Map<ShortJobStatus, Long> frequencyMap(Collection<Job.Status> jobStatuses) {
        Map<ShortJobStatus, Long> frequencyMap = jobStatuses.stream()
                .map(SHORT_STATUSES::get)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        EnumSet.allOf(ShortJobStatus.class).forEach(v -> frequencyMap.putIfAbsent(v, 0L));
        return frequencyMap;
    }

}
