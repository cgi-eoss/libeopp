package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.file.FileMetas;
import com.cgi.eoss.eopp.resource.EoppResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class PathResolverTest {

    private PathResolver resolver;

    @Before
    public void setUp() throws IOException {
        resolver = new PathResolver();
    }

    @Test
    public void canResolveTest() {
        URI fileUri = URI.create("file:///path/to/file");
        URI httpUri = URI.create("http://example.com/file");

        assertThat(resolver.canResolve(fileUri)).isTrue();
        assertThat(resolver.canResolve(httpUri)).isFalse();
    }

    @Test
    public void resolveUri() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        URI uri = testfile.toUri();

        EoppResource resource = resolver.resolveUri(uri);
        assertThat(resource).isNotNull();
        assertThat(resource.getFile()).isEqualTo(testfile.toFile());
        assertThat(resource.getFileMeta()).isEqualTo(FileMetas.get(testfile));
    }

}
