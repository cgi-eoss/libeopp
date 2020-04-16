package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMetas;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class EoppZipCombiningResourceTest {

    @Test
    public void testZipResource() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));

        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2);

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(ImmutableList.of(
                new ZipCombiningResource.ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipCombiningResource.ZipResourceEntry("subdir1/testfile", resource),
                new ZipCombiningResource.ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipCombiningResource.ZipResourceEntry("subdir2/testfile2", resource2)
        ), true);
        assertThat(zipResource.getDescription()).isEqualTo("ZipFile [ " + resource.getDescription() + "," + resource2.getDescription() + " ]");
        assertThat(zipResource.getFileMeta().getChecksum()).isNotEmpty();
        assertThat(zipResource.getOriginalSize()).isEqualTo(resource.contentLength() + resource2.contentLength());

        try (ZipInputStream zis = new ZipInputStream(zipResource.getInputStream())) {
            ZipEntry zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir1/");
            assertThat(zipEntry.isDirectory()).isTrue();

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir1/testfile");
            assertThat(zipEntry.isDirectory()).isFalse();
            assertThat(FileMetas.checksum(ByteSource.wrap(ByteStreams.toByteArray(zis)))).isEqualTo(FileMetas.get(testfile).getChecksum());

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir2/");
            assertThat(zipEntry.isDirectory()).isTrue();

            zipEntry = zis.getNextEntry();
            assertThat(zipEntry.getName()).isEqualTo("subdir2/testfile2");
            assertThat(zipEntry.isDirectory()).isFalse();
            assertThat(FileMetas.checksum(ByteSource.wrap(ByteStreams.toByteArray(zis)))).isEqualTo(FileMetas.get(testfile2).getChecksum());
        }
    }

}