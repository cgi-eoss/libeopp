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

import java.util.List;

public interface StepOperatorEventDispatcher {

    /**
     * <p>Notifies listeners that a StepInstance has been lazily evaluated by the {@link StepOperator} to produce new
     * sub-steps.</p>
     * <p><strong>Note:</strong> The default AbstractStepOperator does not schedule these steps directly.</p>
     *
     * @param parentStep The StepInstance whose multiplicity has dynamically created sub-steps, by parallel inputs or
     *                   parameters, or a nested workflow evaluation.
     * @param subSteps   The StepInstances created by the parentStep.
     */
    void stepExpanded(StepInstance parentStep, List<StepInstance> subSteps);

}