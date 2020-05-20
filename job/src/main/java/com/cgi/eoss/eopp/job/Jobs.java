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
