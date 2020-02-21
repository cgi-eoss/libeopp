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
import com.cgi.eoss.eopp.job.StepInstanceId;
import com.cgi.eoss.eopp.job.StepInstances;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <p>Convenience base class for {@link StepOperator} implementations supporting parallel step execution.</p>
 * <p>Provides a fixed size thread pool to limit concurrent execution, includes timeout behaviour, and handles step
 * multiplicity.</p>
 */
public abstract class AbstractStepOperator implements com.cgi.eoss.eopp.executor.StepOperator {
    private static final Logger log = getLogger(AbstractStepOperator.class);

    private final ConcurrentMap<StepInstanceId, ListenableFuture<StepInstance>> stepFutures = new ConcurrentHashMap<>();
    private final ListeningExecutorService stepExecutorService;
    private final ListeningScheduledExecutorService stepTimeoutExecutorService;
    private final Duration stepExecutionTimeout;

    /**
     * <p>Create a new StepOperator with a maximum of 8 concurrent steps, with no timeout.</p>
     */
    public AbstractStepOperator() {
        this(8, Duration.ofSeconds(-1));
    }

    /**
     * <p>Create a new StepOperator for parallel execution.</p>
     *
     * @param maxConcurrentSteps   The maximum number of concurrent steps to run.
     * @param stepExecutionTimeout The maximum duration before cancellation for any single step. If this is negative,
     *                             the timeout is disabled.
     */
    public AbstractStepOperator(int maxConcurrentSteps, Duration stepExecutionTimeout) {
        this.stepExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxConcurrentSteps));
        this.stepTimeoutExecutorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(maxConcurrentSteps));
        this.stepExecutionTimeout = stepExecutionTimeout;
    }

    @Override
    public ListenableFuture<StepInstance> execute(StepInstance stepInstance) {
        // TODO Handle step multiplicity
        log.debug("{}::{} executing", stepInstance.getJobUuid(), stepInstance.getIdentifier());
        ListenableFuture<StepInstance> stepFuture = getStepFuture(stepInstance);
        stepFutures.put(StepInstances.getId(stepInstance), stepFuture);
        return stepFuture;
    }

    @Override
    public void cleanUp(StepInstance stepInstance) {
        log.debug("{}::{} cleaning up", stepInstance.getJobUuid(), stepInstance.getIdentifier());
        StepInstanceId stepInstanceId = StepInstances.getId(stepInstance);
        Optional.ofNullable(stepFutures.remove(stepInstanceId))
                .ifPresent(f -> f.cancel(true));
        operatorCleanUp(stepInstance);
    }

    @Override
    public ListenableFuture<StepInstance> ensureScheduled(StepInstance stepInstance) {
        log.debug("{}::{} ensuring scheduled", stepInstance.getJobUuid(), stepInstance.getIdentifier());
        return Optional.ofNullable(stepFutures.get(StepInstances.getId(stepInstance)))
                .orElseGet(() -> {
                    // We have no knowledge of this step; clean up and run again
                    cleanUp(stepInstance);
                    return execute(stepInstance);
                });
    }

    /**
     * @return A Callable which executes the given StepInstance. This should block until the step has finished
     * executing, and return the completed StepInstance, i.e. with final results for
     * {@link StepInstance#getOutputsList()}.
     */
    protected abstract Callable<StepInstance> getExecutionCallable(StepInstance stepInstance);

    /**
     * <p>Perform any operator-specific cleanup for the given StepInstance.</p>
     */
    protected abstract void operatorCleanUp(StepInstance stepInstance);

    /**
     * @return The future representing the step execution Callable. May be wrapped with the configured
     * {@link #stepExecutionTimeout} for this operator.
     */
    private ListenableFuture<StepInstance> getStepFuture(StepInstance stepInstance) {
        if (stepExecutionTimeout.isNegative()) {
            return stepExecutorService.submit(getExecutionCallable(stepInstance));
        } else {
            return Futures.withTimeout(stepExecutorService.submit(getExecutionCallable(stepInstance)), stepExecutionTimeout, stepTimeoutExecutorService);
        }
    }

}
