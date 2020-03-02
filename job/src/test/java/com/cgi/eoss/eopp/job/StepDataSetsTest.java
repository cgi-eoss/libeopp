package com.cgi.eoss.eopp.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URI;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

@RunWith(JUnit4.class)
public class StepDataSetsTest {
    private String jobUuid;
    private StepDataSet stepDataSet;

    @Before
    public void setUp() {
        jobUuid = UUID.randomUUID().toString();
        stepDataSet = StepDataSet.newBuilder()
                .setStepIdentifier("step-1")
                .setIdentifier("output-1")
                .setStepOutput(StepOutput.newBuilder()
                        .addFilePaths("subdir1/file1")
                        .addFilePaths("subdir2/file2")
                        .build())
                .build();
    }

    @Test
    public void createStepOutputURI() {
        URI uri = StepDataSets.createStepOutputURI(jobUuid, stepDataSet);
        URI expected = URI.create("eopp+stepoutput://" + jobUuid + "/step-1?outputIdentifier=output-1");
        assertThat(uri).isEqualTo(expected);
    }

    @Test
    public void createStepOutputURIFileSelection() {
        URI uri = StepDataSets.createStepOutputURI(jobUuid, stepDataSet, "subdir1/file1");
        URI expected = URI.create("eopp+stepoutput://" + jobUuid + "/step-1?outputIdentifier=output-1&file=subdir1/file1");
        assertThat(uri).isEqualTo(expected);
    }

    @Test
    public void fromStepOutputURI() {
        StepDataSet stepDataSet = StepDataSets.fromStepOutputURI(URI.create("eopp+stepoutput://" + jobUuid + "/step-1?outputIdentifier=output-1"));
        assertThat(stepDataSet).isEqualTo(stepDataSet.toBuilder().clearStepOutput().build());
    }

    @Test
    public void fromStepOutputURIFileSelection() {
        StepDataSet stepDataSet = StepDataSets.fromStepOutputURI(URI.create("eopp+stepoutput://" + jobUuid + "/step-1?outputIdentifier=output-1&file=subdir1/file1"));
        assertThat(stepDataSet).isEqualTo(stepDataSet.toBuilder()
                .clearStepOutput().setStepOutput(StepOutput.newBuilder()
                        .addFilePaths("subdir1/file1")
                        .build()).build());
    }

}