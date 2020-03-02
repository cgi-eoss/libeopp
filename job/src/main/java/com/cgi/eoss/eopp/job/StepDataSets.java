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

package com.cgi.eoss.eopp.job;

import com.google.common.base.Preconditions;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>Utility methods for working with {@link StepDataSet} objects.</p>
 */
public class StepDataSets {

    private static final String STEP_OUTPUT_URI_SCHEME = "eopp+stepoutput";

    private StepDataSets() {
        // no-op for utility class
    }

    /**
     * @return A canonical URI describing the step output files identified by the given parameters.
     * @see #fromStepOutputURI(URI)
     */
    public static URI createStepOutputURI(String jobUuid, StepDataSet sourceDataSet, String... outputFile) {
        UriBuilder uriBuilder = new DefaultUriBuilderFactory().builder()
                .scheme(STEP_OUTPUT_URI_SCHEME)
                .host(jobUuid)
                .path(sourceDataSet.getStepIdentifier())
                .queryParam("outputIdentifier", sourceDataSet.getIdentifier());
        return outputFile.length > 0
                ? uriBuilder.queryParam("file", Arrays.asList(outputFile)).build()
                : uriBuilder.build();
    }

    /**
     * @return A StepDataSet corresponding to the step output files identified by the given URI.
     * @see #createStepOutputURI(String, StepDataSet, String...)
     */
    public static StepDataSet fromStepOutputURI(URI eoppStepOutput) {
        Preconditions.checkArgument(eoppStepOutput.getScheme().equals(STEP_OUTPUT_URI_SCHEME), "Step output dataset URI did not have the required scheme, found: %s", eoppStepOutput.getScheme());
        UriComponents uriComponents = UriComponentsBuilder.fromUri(eoppStepOutput).build();
        StepDataSet.Builder builder = StepDataSet.newBuilder()
                .setStepIdentifier(Objects.requireNonNull(uriComponents.getPath()).substring(1))
                .setIdentifier(uriComponents.getQueryParams().getFirst("outputIdentifier"));
        return uriComponents.getQueryParams().containsKey("file")
                ? builder.setStepOutput(StepOutput.newBuilder().addAllFilePaths(uriComponents.getQueryParams().get("file")).build()).build()
                : builder.build();
    }

}
