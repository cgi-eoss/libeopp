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

package com.cgi.eoss.eopp.executor;

import com.cgi.eoss.eopp.job.StepInstance;

import java.util.concurrent.CompletableFuture;

/**
 * <p>An Exception thrown by a {@link StepOperator} during the execution of a {@link StepInstance}.</p>
 * <p>This may be extracted from the {@link CompletableFuture}&lt;{@link StepInstance}&gt; in the event of non-nominal
 * execution.</p>
 */
public class StepExecutionException extends RuntimeException {

    private final StepInstance stepInstance;
    private final StepInstance.Status status;

    /**
     * <p>Create a new StepExecutionException associated with the given StepInstance, having the given Status.</p>
     */
    public StepExecutionException(String message, StepInstance stepInstance, StepInstance.Status status) {
        super(message);
        this.stepInstance = stepInstance;
        this.status = status;
    }

    /**
     * <p>Create a new StepExecutionException associated with the given StepInstance, having the given Status.</p>
     */
    public StepExecutionException(String message, StepInstance stepInstance, StepInstance.Status status, Throwable cause) {
        super(message, cause);
        this.stepInstance = stepInstance;
        this.status = status;
    }

    /**
     * @return The StepInstance which threw this execution during execution.
     */
    public StepInstance getStepInstance() {
        return stepInstance;
    }

    /**
     * @return The final execution Status of the StepInstance.
     */
    public StepInstance.Status getStatus() {
        return status;
    }
}
