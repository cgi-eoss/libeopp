package com.cgi.eoss.eopp.job;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

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
        return StepInstanceId.newBuilder()
                .setJobUuid(stepInstance.getJobUuid())
                .setIdentifier(stepInstance.getIdentifier())
                .build();
    }

    public static ListMultimap<String, String> parameterValues(StepInstance stepInstance) {
        return stepInstance.getParametersList().stream()
                .collect(ImmutableListMultimap.flatteningToImmutableListMultimap(
                        StepParameterValue::getIdentifier,
                        parameter -> parameter.getValuesList().stream()
                ));
    }

}
