package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMetas;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class EoppPathResourceTest {

    @Test
    public void testResourceProperties() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);

        EoppResource resource = new EoppPathResource(testfile);
        assertThat(resource.getFile()).isEqualTo(testfile.toFile());
        assertThat(resource.getFileMeta()).isEqualTo(FileMetas.get(testfile));
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.isCacheable()).isFalse();
        assertThat(resource.contentLength()).isEqualTo(Files.size(testfile));
        assertThat(resource.shouldRetry(null)).isFalse();
    }

    @Test
    public void testCustomFilename() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);

        EoppResource resource = new EoppPathResource(testfile, "newfilename");
        assertThat(resource.getFileMeta())
                .isEqualTo(FileMetas.get(testfile).toBuilder().setFilename("newfilename").build());
    }

}