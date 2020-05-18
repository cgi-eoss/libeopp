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
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * <p>Convenience base class for {@link StepOperator} implementations supporting parallel step execution.</p>
 * <p>Provides a fixed size thread pool to limit concurrent execution, includes timeout behaviour, and handles step
 * multiplicity.</p>
 * <p>Recursive multiplicity is not supported by this operator; e.g. a nested workflow with parallel step configurations
 * will not execute those sub-steps in parallel.</p>
 */
public abstract class AbstractStepOperator implements com.cgi.eoss.eopp.executor.StepOperator {
    private static final Logger log = getLogger(AbstractStepOperator.class);

    // This needs to be constant as sub-steps may run on different extensions of this class
    private static final EventBus SUB_STEP_NOTIFICATION_BUS = new EventBus("sub-step-event-bus");

    private final StepOperatorEventDispatcher stepOperatorEventDispatcher;
    private final ConcurrentMap<StepInstanceId, ListenableFuture<StepInstance>> stepFutures = new ConcurrentHashMap<>();
    private final ListeningExecutorService stepExecutorService;
    private final ListeningExecutorService lightweightStepExecutorService;
    private final ListeningScheduledExecutorService stepTimeoutExecutorService;
    private final Duration stepExecutionTimeout;

    /**
     * <p>Create a new StepOperator with a maximum of 8 concurrent steps, with no timeout.</p>
     *
     * @param stepOperatorEventDispatcher A handler for events emitted by this operator.
     */
    public AbstractStepOperator(StepOperatorEventDispatcher stepOperatorEventDispatcher) {
        this(stepOperatorEventDispatcher, 8, Duration.ofSeconds(-1));
    }

    /**
     * <p>Create a new StepOperator for parallel execution.</p>
     *
     * @param stepOperatorEventDispatcher A handler for events emitted by this operator.
     * @param maxConcurrentSteps          The maximum number of concurrent steps to run.
     * @param stepExecutionTimeout        The maximum duration before cancellation for any single step. If this is negative,
     */
    public AbstractStepOperator(StepOperatorEventDispatcher stepOperatorEventDispatcher, int maxConcurrentSteps, Duration stepExecutionTimeout) {
        this.stepOperatorEventDispatcher = stepOperatorEventDispatcher;
        this.stepExecutorService = MoreExecutors.listeningDecorator(Executors.newWorkStealingPool(maxConcurrentSteps));
        this.stepTimeoutExecutorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(maxConcurrentSteps));
        this.lightweightStepExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        this.stepExecutionTimeout = stepExecutionTimeout;
    }

    @Override
    public ListenableFuture<StepInstance> execute(StepInstance stepInstance) {
        log.debug("{}::{} executing", stepInstance.getJobUuid(), stepInstance.getIdentifier());
        StepInstanceId stepInstanceId = StepInstances.getId(stepInstance);

        ListenableFuture<StepInstance> stepFuture;
        if (StepInstances.hasMultiplicity(stepInstance) && Strings.isNullOrEmpty(stepInstance.getParentIdentifier())) {
            stepFuture = submitLightweight(stepInstanceId, getParentStepCallable(stepInstance));
        } else {
            if (!Strings.isNullOrEmpty(stepInstance.getParentIdentifier())) {
                // TODO Enable recursive multiplicity
                log.debug("{}::{} has multiplicity, but expansion is skipped as it is already a sub-step", stepInstance.getJobUuid(), stepInstance.getIdentifier());
            }
            stepFuture = submit(stepInstanceId, getExecutionCallable(stepInstance));
        }

        if (!stepInstance.getParentIdentifier().isEmpty()) {
            Futures.addCallback(stepFuture, getSubStepCallback(stepInstance), lightweightStepExecutorService);
        }

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
     * <p>Execute the given StepInstance callable on the default executor.</p>
     * <p>This may be used by subclasses to reattach during {@link #ensureScheduled(StepInstance)} so that the new call
     * shares the configured timeout and concurrency parameters of this base class.</p>
     *
     * @param stepInstanceId The identifier of the step instance being returned by the task.
     * @param task           The callable to return a completed step instance.
     * @return The given callable as a {@link ListenableFuture}, with a timeout if configured.
     */
    protected ListenableFuture<StepInstance> submit(StepInstanceId stepInstanceId, Callable<StepInstance> task) {
        return submit(stepInstanceId, task, stepExecutorService);
    }

    /**
     * <p>Execute the given StepInstance callable on the 'lightweight' executor, not counting towards maximum step concurrency.</p>
     * <p>This may be used by subclasses to reattach during {@link #ensureScheduled(StepInstance)} so that the new call
     * shares the configured timeout of this base class.</p>
     *
     * @param stepInstanceId The identifier of the step instance being returned by the task.
     * @param task           The callable to return a completed step instance.
     * @return The given callable as a {@link ListenableFuture}, with a timeout if configured.
     */
    protected ListenableFuture<StepInstance> submitLightweight(StepInstanceId stepInstanceId, Callable<StepInstance> task) {
        return submit(stepInstanceId, task, lightweightStepExecutorService);
    }

    private ListenableFuture<StepInstance> submit(StepInstanceId stepInstanceId, Callable<StepInstance> task, ListeningExecutorService stepExecutorService) {
        ListenableFuture<StepInstance> stepExecutionFuture = stepExecutorService.submit(task);
        return executeWithTimeout(stepInstanceId, stepExecutionFuture, stepExecutionTimeout);
    }

    /**
     * <p>Execute the given StepInstance with a timeout. If the timeout parameter is negative, the step is executed with
     * no timeout.</p>
     *
     * @param stepExecutionFuture The executing StepInstance.
     * @param timeout             The timeout after which the executing stepExecutionFuture will be cancelled. Ignored
     *                            if negative.
     * @return The given stepExecutionFuture, wrapped with the given timeout.
     */
    private ListenableFuture<StepInstance> executeWithTimeout(StepInstanceId stepInstanceId, ListenableFuture<StepInstance> stepExecutionFuture, Duration timeout) {
        ListenableFuture<StepInstance> stepFuture;
        if (timeout.isNegative()) {
            stepFuture = stepExecutionFuture;
        } else {
            stepFuture = Futures.withTimeout(stepExecutionFuture, timeout, stepTimeoutExecutorService);
        }
        stepFutures.put(stepInstanceId, stepFuture);
        return stepFuture;
    }

    /**
     * <p>Construct a Callable which executes the given StepInstance. This should block until the step has finished
     * executing, and return the completed StepInstance, i.e. with final results for
     * {@link StepInstance#getOutputsList()}.</p>
     * <p>The Callable should throw {@link StepExecutionException} in the event of non-nominal execution.</p>
     */
    protected abstract Callable<StepInstance> getExecutionCallable(StepInstance stepInstance);

    /**
     * <p>Perform any operator-specific cleanup for the given StepInstance.</p>
     */
    protected abstract void operatorCleanUp(StepInstance stepInstance);

    private List<StepInstance> expandStep(StepInstance step) {
        log.debug("{}::{} expanding", step.getJobUuid(), step.getIdentifier());
        List<StepInstance> subSteps = JobGraph.expandStepInstance(step);
        log.debug("{}::{} expanded to produce {} sub-steps", step.getJobUuid(), step.getIdentifier(), subSteps.size());
        return subSteps;
    }

    private Callable<StepInstance> getParentStepCallable(StepInstance stepInstance) {
        List<StepInstance> subSteps = expandStep(stepInstance);
        ParentStepMonitor parentStepMonitor = new ParentStepMonitor(stepInstance, subSteps);
        SUB_STEP_NOTIFICATION_BUS.register(parentStepMonitor);
        stepOperatorEventDispatcher.stepExpanded(stepInstance, subSteps);
        return parentStepMonitor;
    }

    private FutureCallback<StepInstance> getSubStepCallback(StepInstance stepInstance) {
        return new FutureCallback<StepInstance>() {
            @Override
            public void onSuccess(StepInstance result) {
                log.debug("{}::{} Notifying parent step listener of completion", stepInstance.getJobUuid(), stepInstance.getIdentifier());
                SUB_STEP_NOTIFICATION_BUS.post(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                log.debug("{}::{} Notifying parent step listener of error", stepInstance.getJobUuid(), stepInstance.getIdentifier(), throwable);
                ListenableFuture<StepInstance> parentStepFuture = stepFutures.get(StepInstanceId.newBuilder().setJobUuid(stepInstance.getJobUuid()).setIdentifier(stepInstance.getParentIdentifier()).build());
                parentStepFuture.cancel(true);
            }
        };
    }

    /**
     * <p>The 'executable' corresponding to a parent step in the processing graph.</p>
     * <p>Stores a reference to all expected sub-steps, and waits for StepInstance events signifying their completion.
     * These events should be posted by the callback in {@link #execute(StepInstance)}.</p>
     */
    private static class ParentStepMonitor implements Callable<StepInstance> {
        private final StepInstance.Builder step;
        private final Set<StepInstanceId> subStepIds;
        private final CountDownLatch subStepsLatch;

        public ParentStepMonitor(StepInstance step, List<StepInstance> subSteps) {
            this.step = step.toBuilder();
            this.subStepIds = subSteps.stream().map(StepInstances::getId).collect(Collectors.toSet());
            this.subStepsLatch = new CountDownLatch(subSteps.size());
        }

        @Override
        public StepInstance call() throws Exception {
            subStepsLatch.await();
            log.debug("{}::{} all sub-steps completed", step.getJobUuid(), step.getIdentifier());
            return step.build();
        }

        @Subscribe
        public void subStepCompletionEvent(StepInstance subStep) {
            // avoid collecting events from other parent steps
            if (subStepIds.contains(StepInstances.getId(subStep))) {
                // collect sub-step outputs - only NESTED_WORKFLOW OUTPUT steps, or all sub-steps
                if (step.getConfiguration().getExecuteCase() == StepConfiguration.ExecuteCase.NESTED_WORKFLOW
                        && subStep.getType() == StepInstance.Type.OUTPUT) {
                    step.addAllOutputs(subStep.getOutputsList());
                } else if (step.getConfiguration().getExecuteCase() == StepConfiguration.ExecuteCase.STEP) {
                    step.addAllOutputs(subStep.getOutputsList());
                }
                subStepsLatch.countDown();
            }
        }
    }

}
