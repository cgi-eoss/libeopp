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
import com.cgi.eoss.eopp.jobgraph.JobGraph;
import com.cgi.eoss.eopp.workflow.StepConfiguration;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import dev.failsafe.Failsafe;
import dev.failsafe.FailsafeExecutor;
import dev.failsafe.RetryPolicy;
import dev.failsafe.Timeout;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <p>Convenience base class for {@link StepOperator} implementations supporting parallel step execution.</p>
 * <p>Provides a fixed size thread pool to limit concurrent execution, includes timeout behaviour, and handles step
 * multiplicity.</p>
 * <p>Recursive multiplicity is not supported by this operator; e.g. a nested workflow with parallel step configurations
 * will not execute those sub-steps in parallel.</p>
 */
public abstract class AbstractStepOperator implements StepOperator {
    private static final Logger log = getLogger(AbstractStepOperator.class);

    // This needs to be constant as sub-steps may run on different extensions of this class
    private static final EventBus SUB_STEP_NOTIFICATION_BUS = new EventBus("sub-step-event-bus");

    private final StepOperatorEventDispatcher stepOperatorEventDispatcher;
    private final ConcurrentMap<StepInstanceId, CompletableFuture<StepInstance>> stepFutures = new ConcurrentHashMap<>();
    private final ExecutorService stepExecutorService;
    private final ExecutorService lightweightExecutorService;
    private final Duration stepExecutionTimeout;
    private final RetryPolicy<StepInstance> retryPolicy;

    /**
     * <p>Create a new StepOperator with a maximum of 8 concurrent steps, with no timeout.</p>
     *
     * @param stepOperatorEventDispatcher A handler for events emitted by this operator.
     */
    protected AbstractStepOperator(StepOperatorEventDispatcher stepOperatorEventDispatcher) {
        this(stepOperatorEventDispatcher, 8, Duration.ofSeconds(-1));
    }

    /**
     * <p>Create a new StepOperator for parallel execution.</p>
     *
     * @param stepOperatorEventDispatcher A handler for events emitted by this operator.
     * @param maxConcurrentSteps          The maximum number of concurrent steps to run.
     * @param stepExecutionTimeout        The maximum duration before cancellation for any single step. If this is
     *                                    negative, no timeout is enforced. This is not enforced for steps which expand
     *                                    to multiple sub-steps, although each sub-step will run with a timeout.
     */
    protected AbstractStepOperator(StepOperatorEventDispatcher stepOperatorEventDispatcher, int maxConcurrentSteps, Duration stepExecutionTimeout) {
        this(stepOperatorEventDispatcher,
                maxConcurrentSteps,
                stepExecutionTimeout,
                RetryPolicy.<StepInstance>builder().withMaxAttempts(1).build());
    }

    /**
     * <p>Create a new StepOperator for parallel execution.</p>
     *
     * @param stepOperatorEventDispatcher A handler for events emitted by this operator.
     * @param maxConcurrentSteps          The maximum number of concurrent steps to run.
     * @param stepExecutionTimeout        The maximum duration before cancellation for any single step. If this is
     *                                    negative, no timeout is enforced. This is not enforced for steps which expand
     *                                    to multiple sub-steps, although each sub-step will run with a timeout.
     */
    protected AbstractStepOperator(StepOperatorEventDispatcher stepOperatorEventDispatcher, int maxConcurrentSteps, Duration stepExecutionTimeout, RetryPolicy<StepInstance> retryPolicy) {
        this.stepOperatorEventDispatcher = stepOperatorEventDispatcher;
        this.stepExecutorService = Executors.newWorkStealingPool(maxConcurrentSteps);
        this.lightweightExecutorService = Executors.newCachedThreadPool();
        this.stepExecutionTimeout = stepExecutionTimeout;
        this.retryPolicy = retryPolicy;
    }

    @Override
    public CompletableFuture<StepInstance> execute(StepInstance stepInstance) {
        StepInstanceId stepInstanceId = StepInstances.getId(stepInstance);
        return stepFutures.computeIfAbsent(stepInstanceId, it -> computeStepFuture(stepInstance));
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
    public CompletableFuture<StepInstance> ensureScheduled(StepInstance stepInstance) {
        log.debug("{}::{} ensuring scheduled", stepInstance.getJobUuid(), stepInstance.getIdentifier());
        return getStepFuture(StepInstances.getId(stepInstance))
                .orElseGet(() -> {
                    // We have no knowledge of this step; clean up and run again
                    cleanUp(stepInstance);
                    return execute(stepInstance);
                });
    }

    /**
     * <p>Execute the given StepInstance supplier on the default executor.</p>
     * <p>This may be used by subclasses to reattach during {@link #ensureScheduled(StepInstance)} so that the new call
     * shares the configured timeout and concurrency parameters of this base class.</p>
     *
     * @param stepInstance The step instance being executed by the task.
     * @param task         The supplier to return a completed step instance.
     * @return The given supplier as a {@link CompletableFuture}, with a timeout if configured.
     */
    protected CompletableFuture<StepInstance> submit(StepInstance stepInstance, Supplier<StepInstance> task) {
        RetryPolicy<StepInstance> cleanupRetryPolicy = RetryPolicy.builder(retryPolicy.getConfig())
                .onFailedAttempt(event -> log.trace("{}::{} failed attempt {}/{}", stepInstance.getJobUuid(), stepInstance.getIdentifier(), event.getAttemptCount(), retryPolicy.getConfig().getMaxAttempts(), event.getLastException()))
                .onRetry(event -> {
                    log.debug("{}::{} cleaning and retrying (attempt {}/{})", stepInstance.getJobUuid(), stepInstance.getIdentifier(), event.getAttemptCount() + 1, retryPolicy.getConfig().getMaxAttempts());
                    operatorCleanUp(stepInstance);
                })
                .build();

        FailsafeExecutor<StepInstance> failsafeExecutor;
        if (stepExecutionTimeout.isNegative()) {
            failsafeExecutor = Failsafe.with(cleanupRetryPolicy);
        } else {
            failsafeExecutor = Failsafe.with(cleanupRetryPolicy, Timeout.of(stepExecutionTimeout));
        }

        return failsafeExecutor.getStageAsync(() -> CompletableFuture.supplyAsync(task, stepExecutorService));
    }

    /**
     * <p>Execute the given StepInstance supplier on the 'lightweight' executor, not counting towards maximum step
     * concurrency, and ignoring the configured retry policy and timeout for this operator.</p>
     *
     * @param stepInstance The step instance being executed by the task.
     * @param task         The supplier to return a completed step instance.
     * @return The given supplier as a {@link CompletableFuture}.
     */
    protected CompletableFuture<StepInstance> submitLightweight(StepInstance stepInstance, Supplier<StepInstance> task) {
        // Lightweight tasks shouldn't run with timeouts or retry policies as
        // they should be connected internally to tasks which handle that logic
        return CompletableFuture.supplyAsync(task, lightweightExecutorService);
    }

    /**
     * @return The CompletableFuture representing the given step instance, if an execution has been submitted.
     */
    protected Optional<CompletableFuture<StepInstance>> getStepFuture(StepInstanceId stepInstanceId) {
        return Optional.ofNullable(stepFutures.get(stepInstanceId));
    }

    /**
     * <p>Construct a Supplier which executes the given StepInstance. This should block until the step has finished
     * executing, and return the completed StepInstance, i.e. with final results for {@link StepInstance#getOutputsList()}.</p>
     * <p>The Supplier should throw only {@link StepExecutionException} in the event of non-nominal execution.</p>
     */
    protected abstract Supplier<StepInstance> getExecutionSupplier(StepInstance stepInstance);

    /**
     * <p>Perform any operator-specific cleanup for the given StepInstance.</p>
     * <p>This should ensure that all resources created by calls to {@link #getExecutionSupplier(StepInstance)} are
     * cleaned up, for example threads, files, or external processes.</p>
     */
    protected abstract void operatorCleanUp(StepInstance stepInstance);

    private CompletableFuture<StepInstance> computeStepFuture(StepInstance stepInstance) {
        try {
            log.debug("{}::{} executing", stepInstance.getJobUuid(), stepInstance.getIdentifier());

            CompletableFuture<StepInstance> stepFuture;
            if (stepInstance.getParentIdentifier().isEmpty() && StepInstances.hasMultiplicity(stepInstance)) {
                stepFuture = submitLightweight(stepInstance, getParentStepSupplier(stepInstance));
            } else {
                if (!stepInstance.getParentIdentifier().isEmpty() && StepInstances.hasMultiplicity(stepInstance)) {
                    // TODO Enable recursive multiplicity
                    log.debug("{}::{} has multiplicity, but expansion is skipped as it is already a sub-step", stepInstance.getJobUuid(), stepInstance.getIdentifier());
                }
                stepFuture = submit(stepInstance, getExecutionSupplier(stepInstance));
            }

            if (!stepInstance.getParentIdentifier().isEmpty()) {
                stepFuture.whenCompleteAsync(getSubStepCallback(stepInstance), lightweightExecutorService);
            }

            return stepFuture;
        } catch (Exception e) {
            log.error("{}::{} execution failed to start", stepInstance.getJobUuid(), stepInstance.getIdentifier(), e);
            return CompletableFuture.failedFuture(new StepExecutionException(String.format("%s::%s execution failed to start", stepInstance.getJobUuid(), stepInstance.getIdentifier()), stepInstance, StepInstance.Status.FAILED));
        }
    }

    private List<StepInstance> expandStep(StepInstance step) {
        log.debug("{}::{} expanding", step.getJobUuid(), step.getIdentifier());
        List<StepInstance> subSteps = JobGraph.expandStepInstance(step);
        log.debug("{}::{} expanded to produce {} sub-steps", step.getJobUuid(), step.getIdentifier(), subSteps.size());
        return subSteps;
    }

    private Supplier<StepInstance> getParentStepSupplier(StepInstance stepInstance) {
        List<StepInstance> subSteps = expandStep(stepInstance);
        ParentStepMonitor parentStepMonitor = new ParentStepMonitor(stepInstance, subSteps);
        SUB_STEP_NOTIFICATION_BUS.register(parentStepMonitor);
        lightweightExecutorService.submit(() -> stepOperatorEventDispatcher.stepExpanded(stepInstance, subSteps));
        return parentStepMonitor;
    }

    private BiConsumer<StepInstance, Throwable> getSubStepCallback(StepInstance stepInstance) {
        return (result, throwable) -> {
            if (result != null) {
                log.debug("{}::{} Notifying parent step listener of completion", stepInstance.getJobUuid(), stepInstance.getIdentifier());
                SUB_STEP_NOTIFICATION_BUS.post(result);
            } else if (throwable != null) {
                log.debug("{}::{} Notifying parent step listener of error", stepInstance.getJobUuid(), stepInstance.getIdentifier(), throwable);
                CompletableFuture<StepInstance> parentStepFuture = stepFutures.get(StepInstanceId.newBuilder().setJobUuid(stepInstance.getJobUuid()).setIdentifier(stepInstance.getParentIdentifier()).build());
                parentStepFuture.cancel(true);
            } else {
                // This should never actually happen
                log.warn("{}::{} Sub-step callback received null result and exception", stepInstance.getJobUuid(), stepInstance.getIdentifier());
            }
        };
    }

    /**
     * <p>The 'executable' corresponding to a parent step in the processing graph.</p>
     * <p>Stores a reference to all expected sub-steps, and waits for StepInstance events signifying their completion.
     * These events should be posted by the callback in {@link #execute(StepInstance)}.</p>
     */
    private static class ParentStepMonitor implements Supplier<StepInstance> {
        private final StepInstance.Builder step;
        private final Set<StepInstanceId> subStepIds;
        private final CountDownLatch subStepsLatch;

        public ParentStepMonitor(StepInstance step, List<StepInstance> subSteps) {
            this.step = step.toBuilder();
            this.subStepIds = subSteps.stream().map(StepInstances::getId).collect(Collectors.toSet());
            this.subStepsLatch = new CountDownLatch(subSteps.size());
        }

        @Override
        public StepInstance get() {
            try {
                subStepsLatch.await();
                log.debug("{}::{} all sub-steps completed", step.getJobUuid(), step.getIdentifier());
                return step.build();
            } catch (InterruptedException e) {
                log.error("{}::{} interrupted waiting for sub-steps", step.getJobUuid(), step.getIdentifier());
                Thread.currentThread().interrupt();
                throw new StepExecutionException("Interrupted waiting for sub-steps", step.build(), StepInstance.Status.FAILED);
            }
        }

        @Subscribe
        public void subStepCompletionEvent(StepInstance subStep) {
            // avoid collecting events from other parent steps
            if (subStepIds.contains(StepInstances.getId(subStep))) {
                // collect sub-step outputs - only NESTED_WORKFLOW OUTPUT steps, or all sub-steps
                if ((step.getConfiguration().getExecuteCase() == StepConfiguration.ExecuteCase.NESTED_WORKFLOW && subStep.getType() == StepInstance.Type.OUTPUT)
                        || step.getConfiguration().getExecuteCase() == StepConfiguration.ExecuteCase.STEP) {
                    step.addAllOutputs(subStep.getOutputsList());
                }
                subStepsLatch.countDown();
            }
        }
    }

}
