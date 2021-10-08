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

import com.cgi.eoss.eopp.workflow.StepConfiguration;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;

import java.util.Objects;

/**
 * <p>Utility methods for working with {@link StepInstance} objects.</p>
 */
public final class StepInstances {

    private StepInstances() {
        // no-op for utility class
    }

    /**
     * @return The platform-wide unique identifier for the given StepInstance.
     */
    public static StepInstanceId getId(StepInstance stepInstance) {
        return idOf(stepInstance.getJobUuid(), stepInstance.getIdentifier());
    }

    /**
     * @return The platform-wide unique identifier for the given StepInstance properties.
     */
    public static StepInstanceId idOf(String jobUuid, String identifier) {
        return StepInstanceId.newBuilder()
                .setJobUuid(jobUuid)
                .setIdentifier(identifier)
                .build();
    }

    /**
     * @return The given StepInstance's parameter values, wrapped in a ListMultimap.
     */
    public static ListMultimap<String, String> parameterValues(StepInstance stepInstance) {
        return stepInstance.getParametersList().stream()
                .collect(ImmutableListMultimap.flatteningToImmutableListMultimap(
                        StepParameterValue::getIdentifier,
                        parameter -> parameter.getValuesList().stream()
                ));
    }

    /**
     * @return True if the given StepInstance has any dynamic configuration which should be expanded, instead of
     * executed directly.
     */
    public static boolean hasMultiplicity(StepInstance stepInstance) {
        return stepInstance.getConfiguration().getExecuteCase() == StepConfiguration.ExecuteCase.NESTED_WORKFLOW
                || hasParallelConfiguration(stepInstance);
    }

    /**
     * @return True if the given StepInstance has any parameters marked for parallel processing.
     */
    public static boolean hasParallelConfiguration(StepInstance stepInstance) {
        StepConfiguration configuration = stepInstance.getConfiguration();
        return configuration.getInputLinksList().stream().anyMatch(StepConfiguration.InputLink::getParallel)
                || configuration.getParameterLinksList().stream().anyMatch(StepConfiguration.ParameterLink::getParallel);
    }

    public static StepDataSet getStepInput(StepInstance step, String identifier) {
        return step.getInputsList().stream()
                .filter(it -> identifier.equals(Objects.requireNonNull(it).getIdentifier())).findFirst().orElseThrow();
    }

    public static int getStepInputIdx(StepInstance step, String identifier) {
        return Iterables.indexOf(step.getInputsList(),
                it -> identifier.equals(Objects.requireNonNull(it).getIdentifier()));
    }

    public static StepParameterValue getStepParameter(StepInstance step, String identifier) {
        return step.getParametersList().stream()
                .filter(it -> identifier.equals(Objects.requireNonNull(it).getIdentifier())).findFirst().orElseThrow();
    }

    public static int getStepParameterIdx(StepInstance step, String identifier) {
        return Iterables.indexOf(step.getParametersList(),
                it -> identifier.equals(Objects.requireNonNull(it).getIdentifier()));
    }

}
