package com.cgi.eoss.eopp.resource;


import com.google.common.truth.Truth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

@RunWith(JUnit4.class)
public class ZipResourceEntryTest {
    @Test
    public void testSafeGetFileTime() throws IOException {
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.lastModified()).thenReturn(1L);

        ZipResourceEntry entry = new ZipResourceEntry("test", resource);
        Truth.assertThat(entry.lastModified()).isEqualTo(FileTime.fromMillis(1L));
    }

    @Test
    public void testSafeGetFileTimeException() throws IOException {
        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.lastModified()).thenThrow(new IOException());

        ZipResourceEntry entry = new ZipResourceEntry("test", resource);
        Truth.assertThat(entry.lastModified()).isEqualTo(FileTime.fromMillis(0L));
    }
}
