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

import com.cgi.eoss.eopp.identifier.Identifiers;
import com.cgi.eoss.eopp.job.StepDataSet;
import com.cgi.eoss.eopp.job.StepInstance;
import com.cgi.eoss.eopp.job.StepInstanceId;
import com.cgi.eoss.eopp.job.StepInstances;
import com.cgi.eoss.eopp.job.StepOutput;
import com.cgi.eoss.eopp.job.StepParameterValue;
import com.cgi.eoss.eopp.workflow.DataSources;
import com.cgi.eoss.eopp.workflow.Output;
import com.cgi.eoss.eopp.workflow.Parameter;
import com.cgi.eoss.eopp.workflow.Step;
import com.cgi.eoss.eopp.workflow.StepConfiguration;
import com.cgi.eoss.eopp.workflow.Workflow;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.util.concurrent.MoreExecutors;
import dev.failsafe.RetryPolicy;
import dev.failsafe.TimeoutExceededException;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(JUnit4.class)
public class AbstractStepOperatorTest {

    private String jobId;

    @Before
    public void setUp() {
        jobId = UUID.randomUUID().toString();
    }

    @Test
    public void testExecute() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        CompletableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .build());
        StepInstance stepInstance = execute.get();
        assertThat(stepInstance.getOutputsCount()).isEqualTo(1);
    }

    @Test
    public void testExecuteWithFailure() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        CompletableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("THROW")
                        .addValues("My failure message")
                        .build())
                .build());
        try {
            StepInstance stepInstance = execute.get();
            fail("Expected StepExecutionException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(StepExecutionException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("My failure message");
            assertThat(((StepExecutionException) e.getCause()).getStatus()).isEqualTo(StepInstance.Status.FAILED);
        }
    }

    @Test
    public void testExecuteConcurrent() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<Void> futures = CompletableFuture.allOf(IntStream.range(1, 9)
                .mapToObj(i -> StepInstance.newBuilder()
                        .setIdentifier("test-step-" + i)
                        .setJobUuid(jobId)
                        .addParameters(StepParameterValue.newBuilder()
                                .setIdentifier("DURATION")
                                .addValues(String.valueOf(Duration.ofMillis(100).toMillis()))
                                .build())
                        .build())
                .map(stepOperator::execute)
                .toArray(CompletableFuture[]::new));

        futures.get();
        Duration elapsed = stopwatch.stop().elapsed();

        // Simply ensure that the total duration is less than the sum of all step sleeps, i.e. steps ran in parallel
        assertThat(elapsed).isLessThan(stepOperator.durations.values().stream().reduce(Duration.ZERO, Duration::plus));
    }

    @Test
    public void testExecuteWithTimeout() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofMillis(100));
        CompletableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("DURATION")
                        .addValues(String.valueOf(Duration.ofSeconds(10).toMillis()))
                        .build())
                .build());
        try {
            StepInstance stepInstance = execute.get();
            fail("Expected TimeoutException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(TimeoutExceededException.class);
        }
    }

    @Test
    public void testCleanUpCancelsStep() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("DURATION")
                        .addValues(String.valueOf(Duration.ofSeconds(10).toMillis()))
                        .build())
                .build();

        CountDownLatch assertedLatch = new CountDownLatch(1);

        CompletableFuture<StepInstance> execute = stepOperator.execute(stepInstance);
        execute.whenCompleteAsync((result, t) -> {
            if (result != null) {
                fail("Expected CompletableFuture to be cancelled");
            }
            assertThat(t).isInstanceOf(CancellationException.class);
            assertedLatch.countDown();
        }, MoreExecutors.directExecutor());

        // Wait for the step to start, then cancel it
        Awaitility.await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
                    assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
                });

        stepOperator.cleanUp(stepInstance);

        // Ensure we've done our assertions in the CompletableFuture callback
        assertedLatch.await();

        assertThat(execute.isCancelled()).isTrue();
        assertThat(stepOperator.cleanedUp).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.cleanedUp.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
    }

    @Test
    public void testEnsureScheduled() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .build();

        StepInstance result = stepOperator.ensureScheduled(stepInstance).get();

        // because the ListenableFuture was not registered, the step was re-run
        // first cleanUp was called
        assertThat(stepOperator.cleanedUp).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.cleanedUp.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
        // then the step was executed once
        assertThat(result.getOutputsCount()).isEqualTo(1);
        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
    }

    @Test
    public void testParallelNestedWorkflowExpansion() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService);
        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("nested-workflow-parameter")
                        .addValues("param1")
                        .addValues("param2")
                        .addValues("param3")
                        .build())
                .setConfiguration(StepConfiguration.newBuilder()
                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                .setIdentifier("nested-workflow-parameter")
                                .setParallel(true)
                                .build())
                        .setNestedWorkflow(Workflow.newBuilder()
                                .addParameters(Parameter.newBuilder()
                                        .setIdentifier("nested-workflow-parameter")
                                        .setMinOccurs(1)
                                        .setMaxOccurs(1)
                                        .build())
                                .addStepConfigurations(StepConfiguration.newBuilder()
                                        .setIdentifier("nested-workflow-step")
                                        .setStep(Step.newBuilder()
                                                .setIdentifier(Identifiers.parse("nested-workflow-step:1.0.0"))
                                                .addParameters(Parameter.newBuilder()
                                                        .setIdentifier("nested-step-parameter")
                                                        .setMinOccurs(1)
                                                        .setMaxOccurs(1)
                                                        .build())
                                                .addOutputs(Output.newBuilder()
                                                        .setIdentifier("nested-workflow-step-output")
                                                        .setMinOccurs(1)
                                                        .setMaxOccurs(1)
                                                        .build())
                                                .build())
                                        .addParameterLinks(StepConfiguration.ParameterLink.newBuilder()
                                                .setIdentifier("nested-step-parameter")
                                                .setWorkflowParameter("nested-workflow-parameter")
                                                .build())
                                        .build())
                                .addOutputs(Output.newBuilder()
                                        .setIdentifier("nested-workflow-output")
                                        .setMinOccurs(1)
                                        .setMaxOccurs(1)
                                        .setSources(DataSources.newBuilder()
                                                .addStepOutputs(DataSources.StepOutput.newBuilder()
                                                        .setStepIdentifier("nested-workflow-step")
                                                        .setOutputIdentifier("nested-workflow-step-output")
                                                        .build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        CompletableFuture<StepInstance> parentStepFuture = stepOperator.execute(stepInstance);
        StepInstance finished = parentStepFuture.get(5, TimeUnit.SECONDS);

        assertThat(stepOperatorEventService.expanded).hasSize(6); // six total steps in the expanded parallel nested workflow
        assertThat(finished.getOutputsCount()).isEqualTo(3); // three OUTPUT steps in the expanded parallel nested workflow
    }

    @Test
    public void testRetryPolicySuccess() throws ExecutionException, InterruptedException {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofSeconds(-1),
                RetryPolicy.<StepInstance>builder().withMaxAttempts(3).build());

        CompletableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .build());
        StepInstance stepInstance = execute.get();
        assertThat(stepInstance.getOutputsCount()).isEqualTo(1);

        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
        assertThat(stepOperator.cleanedUp).doesNotContainKey(StepInstances.getId(stepInstance));
    }

    @Test
    public void testRetryPolicyMultipleAttempts() {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofSeconds(-1),
                RetryPolicy.<StepInstance>builder().withMaxAttempts(3).build());

        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("THROW")
                        .addValues("My failure message")
                        .build())
                .build();
        CompletableFuture<StepInstance> execute = stepOperator.execute(stepInstance);

        try {
            execute.get();
            fail("Expected StepExecutionException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(StepExecutionException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("My failure message");
            assertThat(((StepExecutionException) e.getCause()).getStatus()).isEqualTo(StepInstance.Status.FAILED);
        }

        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(3);
        assertThat(stepOperator.cleanedUp).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.cleanedUp.get(StepInstances.getId(stepInstance)).get()).isEqualTo(2); // one cleanup before each retry, the overall cleanup should be performed by the caller
    }

    @Test
    public void testRetryPolicyHandleMatched() {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofSeconds(-1),
                RetryPolicy.<StepInstance>builder().withMaxAttempts(3)
                        .handleIf(t -> t.getMessage().equals("My failure message"))
                        .build());

        // Failsafe will enter retry logic if the exception matches the handleIf predicate
        // i.e. we should see all 3 attempts

        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("THROW")
                        .addValues("My failure message")
                        .build())
                .build();
        CompletableFuture<StepInstance> execute = stepOperator.execute(stepInstance);

        try {
            execute.get();
            fail("Expected StepExecutionException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(StepExecutionException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("My failure message");
            assertThat(((StepExecutionException) e.getCause()).getStatus()).isEqualTo(StepInstance.Status.FAILED);
        }

        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(3);
        assertThat(stepOperator.cleanedUp).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.cleanedUp.get(StepInstances.getId(stepInstance)).get()).isEqualTo(2); // one cleanup before each retry, the overall cleanup should be performed by the caller
    }

    @Test
    public void testRetryPolicyHandleUnmatched() {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofSeconds(-1),
                RetryPolicy.<StepInstance>builder().withMaxAttempts(3)
                        .handleIf(t -> t.getCause() instanceof IOException)
                        .build());

        // Failsafe will enter retry logic if the exception matches the handleIf predicate
        // i.e. we should see only 1 attempt rather than 3

        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("THROW")
                        .addValues("My failure message")
                        .build())
                .build();
        CompletableFuture<StepInstance> execute = stepOperator.execute(stepInstance);

        try {
            execute.get();
            fail("Expected StepExecutionException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(StepExecutionException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("My failure message");
            assertThat(((StepExecutionException) e.getCause()).getStatus()).isEqualTo(StepInstance.Status.FAILED);
        }

        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
        assertThat(stepOperator.cleanedUp).doesNotContainKey(StepInstances.getId(stepInstance));
    }

    @Test
    public void testRetryPolicyAbortIfMatched() {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, 8, Duration.ofSeconds(-1),
                RetryPolicy.<StepInstance>builder().withMaxAttempts(3)
                        .abortIf((stepInstance, throwable) -> throwable.getCause() instanceof IOException)
                        .build());
        stepOperator.throwCause.set(new IOException("Underlying failure"));

        // Failsafe will abort if the result matches the abortIf predicate
        // i.e. we should see only 1 attempt rather than 3

        StepInstance stepInstance = StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("THROW")
                        .addValues("My failure message")
                        .build())
                .build();
        CompletableFuture<StepInstance> execute = stepOperator.execute(stepInstance);

        try {
            execute.get();
            fail("Expected StepExecutionException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasCauseThat().isInstanceOf(StepExecutionException.class);
            assertThat(e).hasCauseThat().hasMessageThat().isEqualTo("My failure message");
            assertThat(((StepExecutionException) e.getCause()).getStatus()).isEqualTo(StepInstance.Status.FAILED);
        }

        assertThat(stepOperator.started).containsKey(StepInstances.getId(stepInstance));
        assertThat(stepOperator.started.get(StepInstances.getId(stepInstance)).get()).isEqualTo(1);
        assertThat(stepOperator.cleanedUp).doesNotContainKey(StepInstances.getId(stepInstance));
    }

    private static class TestStepOperator extends AbstractStepOperator {
        private static final Logger log = getLogger(TestStepOperator.class);

        private final ConcurrentMap<StepInstanceId, AtomicInteger> started = new ConcurrentHashMap<>();
        private final ConcurrentMap<StepInstanceId, AtomicInteger> cleanedUp = new ConcurrentHashMap<>();
        private final ConcurrentMap<StepInstanceId, Duration> durations = new ConcurrentHashMap<>();
        private final AtomicReference<Throwable> throwCause = new AtomicReference<>();

        private TestStepOperator(TestStepOperatorEventDispatcher stepOperatorEventService) {
            super(stepOperatorEventService);
            stepOperatorEventService.attach(this);
        }

        private TestStepOperator(TestStepOperatorEventDispatcher stepOperatorEventService, int maxConcurrentSteps, Duration stepExecutionTimeout) {
            super(stepOperatorEventService, maxConcurrentSteps, stepExecutionTimeout);
            stepOperatorEventService.attach(this);
        }

        private TestStepOperator(TestStepOperatorEventDispatcher stepOperatorEventService, int maxConcurrentSteps, Duration stepExecutionTimeout, RetryPolicy<StepInstance> retryPolicy) {
            super(stepOperatorEventService, maxConcurrentSteps, stepExecutionTimeout, retryPolicy);
            stepOperatorEventService.attach(this);
        }

        @Override
        protected Supplier<StepInstance> getExecutionSupplier(StepInstance stepInstance) {
            AtomicInteger startedCount = started.computeIfAbsent(StepInstances.getId(stepInstance), stepInstanceId -> new AtomicInteger());
            return () -> {
                log.debug("{}::{} starting execution", stepInstance.getJobUuid(), stepInstance.getIdentifier());
                startedCount.incrementAndGet();
                Stopwatch stepTimer = Stopwatch.createStarted();
                ListMultimap<String, String> parameterValues = StepInstances.parameterValues(stepInstance);
                parameterValues.get("DURATION").stream().findFirst()
                        .ifPresent(sleepParameter -> {
                            Duration duration = Duration.ofMillis(Long.parseLong(sleepParameter));
                            Stopwatch stopwatch = Stopwatch.createStarted();
                            Awaitility.await()
                                    .pollInterval(Duration.ofMillis(1))
                                    .until(() -> stopwatch.elapsed().compareTo(duration) >= 0);
                        });
                parameterValues.get("THROW").stream().findFirst()
                        .ifPresent(throwParameter -> {
                            Optional.ofNullable(throwCause.get()).ifPresentOrElse(
                                    throwable -> {
                                        throw new StepExecutionException(throwParameter, stepInstance, StepInstance.Status.FAILED, throwable);
                                    },
                                    () -> {
                                        throw new StepExecutionException(throwParameter, stepInstance, StepInstance.Status.FAILED);
                                    }
                            );
                        });
                durations.put(StepInstances.getId(stepInstance), stepTimer.stop().elapsed());
                log.debug("{}::{} completed", stepInstance.getJobUuid(), stepInstance.getIdentifier());
                return stepInstance.toBuilder()
                        .addOutputs(StepDataSet.newBuilder()
                                .setIdentifier("out1")
                                .setStepIdentifier(stepInstance.getIdentifier())
                                .setStatus(StepDataSet.Status.FINAL)
                                .setStepOutput(StepOutput.newBuilder()
                                        .addFilePaths("path/to/output-file")
                                        .build())
                                .build())
                        .build();
            };
        }

        @Override
        protected void operatorCleanUp(StepInstance stepInstance) {
            cleanedUp.computeIfAbsent(StepInstances.getId(stepInstance), stepInstanceId -> new AtomicInteger())
                    .incrementAndGet();
        }
    }

    private static class TestStepOperatorEventDispatcher implements StepOperatorEventDispatcher {
        private final SetMultimap<StepInstanceId, StepInstanceId> expanded = Multimaps.synchronizedSetMultimap(HashMultimap.create());
        private TestStepOperator testStepOperator;

        @Override
        public void stepExpanded(StepInstance parentStep, List<StepInstance> subSteps) {
            for (StepInstance subStep : subSteps) {
                expanded.put(StepInstances.getId(parentStep), StepInstances.getId(subStep));
                testStepOperator.execute(subStep); // simply executes the substeps on the original executor
            }
        }

        public void attach(TestStepOperator testStepOperator) {
            this.testStepOperator = testStepOperator;
        }
    }

}