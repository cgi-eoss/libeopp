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

public interface StepOperator {

    /**
     * <p>Execute this {@link StepInstance} in the configured environment.</p>
     * <p>Implementations of this method are expected to launch the StepInstance asynchronously and eagerly return the
     * {@link CompletableFuture}, unless otherwise specified by the implementing class.</p>
     *
     * @param stepInstance The configured step to be executed.
     * @return A {@link CompletableFuture} resolving to the completed {@link StepInstance}, i.e. with final results for
     * {@link StepInstance#getOutputsList()}. If the StepInstance fails to complete nominally, the CompletableFuture
     * will throw a {@link StepExecutionException}.
     */
    CompletableFuture<StepInstance> execute(StepInstance stepInstance);

    /**
     * <p>Clean up any active resources associated with this step.</p>
     * <p>Note that this should not remove resources required by other steps in the job, including step outputs.</p>
     *
     * @param stepInstance The step to be cleaned up.
     */
    void cleanUp(StepInstance stepInstance);

    /**
     * <p>Synchronise the given {@link StepInstance} with the current system state managed by the operator.</p>
     * <p>This ensures that the step process is either currently queued or running or has terminated. If none of these
     * things are true, the step is restarted from a clean state.</p>
     *
     * @param stepInstance The configured step to be synchronised or executed if necessary.
     * @return A {@link CompletableFuture} resolving to the completed {@link StepInstance}, i.e. with final results for
     * {@link StepInstance#getOutputsList()}. If the StepInstance fails to complete nominally, the CompletableFuture
     * will throw a {@link StepExecutionException}.
     */
    CompletableFuture<StepInstance> ensureScheduled(StepInstance stepInstance);

}
