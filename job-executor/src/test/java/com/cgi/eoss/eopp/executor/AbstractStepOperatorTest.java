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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

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
        ListenableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
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
        ListenableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
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
        ListenableFuture<List<StepInstance>> futures = Futures.allAsList(IntStream.range(1, 9)
                .mapToObj(i -> StepInstance.newBuilder()
                        .setIdentifier("test-step-" + i)
                        .setJobUuid(jobId)
                        .addParameters(StepParameterValue.newBuilder()
                                .setIdentifier("SLEEP")
                                .addValues(String.valueOf(Duration.ofMillis(100).toMillis()))
                                .build())
                        .build())
                .map(stepOperator::execute)
                .toArray((IntFunction<ListenableFuture<StepInstance>[]>) ListenableFuture[]::new));

        futures.get();
        Duration elapsed = stopwatch.stop().elapsed();

        // Simply ensure that the total duration is less than the sum of all step sleeps, i.e. steps ran in parallel
        assertThat(elapsed).isLessThan(Duration.ofMillis(8 * 100));
    }

    @Test
    public void testExecuteWithTimeout() throws Exception {
        TestStepOperatorEventDispatcher stepOperatorEventService = new TestStepOperatorEventDispatcher();
        TestStepOperator stepOperator = new TestStepOperator(stepOperatorEventService, Duration.ofMillis(100));
        ListenableFuture<StepInstance> execute = stepOperator.execute(StepInstance.newBuilder()
                .setIdentifier("test-step")
                .setJobUuid(jobId)
                .addParameters(StepParameterValue.newBuilder()
                        .setIdentifier("SLEEP")
                        .addValues(String.valueOf(Duration.ofSeconds(10).toMillis()))
                        .build())
                .build());
        try {
            StepInstance stepInstance = execute.get();
            fail("Expected TimeoutFutureException");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(ExecutionException.class);
            assertThat(e).hasMessageThat().contains("Timed out");
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
                        .setIdentifier("SLEEP")
                        .addValues(String.valueOf(Duration.ofSeconds(10).toMillis()))
                        .build())
                .build();

        ListenableFuture<StepInstance> execute = stepOperator.execute(stepInstance);
        Futures.addCallback(execute, new FutureCallback<StepInstance>() {
            @Override
            public void onSuccess(StepInstance result) {
                fail("Expected ListenableFuture to be cancelled");
            }

            @Override
            public void onFailure(Throwable t) {
                assertThat(t).isInstanceOf(CancellationException.class);
            }
        }, MoreExecutors.directExecutor());

        stepOperator.cleanUp(stepInstance);

        assertThat(execute.isCancelled()).isTrue();
        assertThat(stepOperator.cleanedUp).contains(StepInstances.getId(stepInstance));
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
        assertThat(stepOperator.cleanedUp).contains(StepInstances.getId(stepInstance));
        // then the step was executed
        assertThat(result.getOutputsCount()).isEqualTo(1);
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

        ListenableFuture<StepInstance> parentStepFuture = stepOperator.execute(stepInstance);
        StepInstance finished = parentStepFuture.get(5, TimeUnit.SECONDS);

        assertThat(stepOperatorEventService.expanded).hasSize(6); // six total steps in the expanded parallel nested workflow
        assertThat(finished.getOutputsCount()).isEqualTo(3); // three OUTPUT steps in the expanded parallel nested workflow
    }

    private static class TestStepOperator extends AbstractStepOperator {
        private List<StepInstanceId> cleanedUp = new CopyOnWriteArrayList<>();

        private TestStepOperator(TestStepOperatorEventDispatcher stepOperatorEventService) {
            super(stepOperatorEventService);
            stepOperatorEventService.attach(this);
        }

        private TestStepOperator(StepOperatorEventDispatcher stepOperatorEventService, Duration stepExecutionTimeout) {
            super(stepOperatorEventService, 8, stepExecutionTimeout);
        }

        @Override
        protected Callable<StepInstance> getExecutionCallable(StepInstance stepInstance) {
            return () -> {
                ListMultimap<String, String> parameterValues = StepInstances.parameterValues(stepInstance);
                parameterValues.get("SLEEP").stream().findFirst()
                        .ifPresent(sleepParameter -> {
                            try {
                                Thread.sleep(Long.parseLong(sleepParameter));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                parameterValues.get("THROW").stream().findFirst()
                        .ifPresent(throwParameter -> {
                            throw new StepExecutionException(throwParameter, stepInstance, StepInstance.Status.FAILED);
                        });
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
            cleanedUp.add(StepInstances.getId(stepInstance));
        }
    }

    private static class TestStepOperatorEventDispatcher implements StepOperatorEventDispatcher {
        private SetMultimap<StepInstanceId, StepInstanceId> expanded = Multimaps.synchronizedSetMultimap(HashMultimap.create());
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