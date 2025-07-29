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

package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMetas;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

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

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        ), true);
        assertThat(zipResource.getDescription()).isEqualTo("ZipFile (compression: -1) [ " + resource.getDescription() + "," + resource2.getDescription() + " ]");
        assertThat(zipResource.getFileMeta().getChecksum()).isNotEmpty();
        assertThat(zipResource.getFileMeta().getFilename()).isEmpty();
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

    @Test
    public void testZipResourceLazy() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));

        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2);

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        ), false);
        assertThat(zipResource.getDescription()).isEqualTo("ZipFile (compression: -1) [ " + resource.getDescription() + "," + resource2.getDescription() + " ]");
        assertThat(zipResource.getFileMeta().getChecksum()).isEmpty();
        assertThat(zipResource.getFileMeta().getFilename()).isEmpty();
        assertThat(zipResource.getOriginalSize()).isEqualTo(-1);

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

    @Test
    public void testZipEagerException() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));


        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2) {
            @Override
            public InputStream getInputStream() throws IOException {
                throw new FileNotFoundException();
            }
        };

        try {
            new EoppZipCombiningResource(List.of(
                    new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                    new ZipResourceEntry("subdir1/testfile", resource),
                    new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                    new ZipResourceEntry("subdir2/testfile2", resource2)
            ), true);
            fail("Expected EoppResourceException");
        } catch (EoppResourceException e) {
            assertThat(e.getCause()).isInstanceOf(FileNotFoundException.class);
        }
    }

    @Test
    public void testZipLazyException() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));


        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2) {
            @Override
            public InputStream getInputStream() throws IOException {
                throw new FileNotFoundException();
            }
        };

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        ), false);
        assertThat(zipResource.getDescription()).isEqualTo("ZipFile (compression: -1) [ " + resource.getDescription() + "," + resource2.getDescription() + " ]");
        assertThat(zipResource.getFileMeta().getChecksum()).isEmpty();
        assertThat(zipResource.getFileMeta().getFilename()).isEmpty();
        assertThat(zipResource.getOriginalSize()).isEqualTo(-1);

        // The stream is returned, but exceptions are suppressed inside
        try (ZipInputStream zis = new ZipInputStream(zipResource.getInputStream())) {
            zis.getNextEntry();
            fail("Expected EoppResourceException");
        } catch (EoppResourceException e) {
            assertThat(e.getSuppressed()[0].getCause()).isInstanceOf(FileNotFoundException.class);
        }
    }

    @Test
    public void testEqualsHashCode() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));

        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2);

        List<ZipResourceEntry> entries = List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        );

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(entries, true);
        EoppZipCombiningResource zipResource2 = new EoppZipCombiningResource(entries, false);

        assertThat(zipResource).isEqualTo(zipResource2);
        assertThat(zipResource.hashCode()).isEqualTo(zipResource2.hashCode());
    }

    @Test
    public void testZipResourceReadHeader() throws IOException {
        Path testfile = Files.createTempFile("testfile", null);
        Files.write(testfile, Arrays.asList("first", "second", "third"));
        Path testfile2 = Files.createTempFile("testfile2", null);
        Files.write(testfile2, Arrays.asList("fourth", "fifth", "sixth"));

        EoppResource resource = new EoppPathResource(testfile);
        EoppResource resource2 = new EoppPathResource(testfile2);

        EoppZipCombiningResource zipResource = new EoppZipCombiningResource(List.of(
                new ZipResourceEntry("subdir1/", FileTime.fromMillis(resource.lastModified())),
                new ZipResourceEntry("subdir1/testfile", resource),
                new ZipResourceEntry("subdir2/", FileTime.fromMillis(resource2.lastModified())),
                new ZipResourceEntry("subdir2/testfile2", resource2)
        ), true);

        // Read the first four bytes: \x50\x4b\x03\x04
        try (InputStream is = zipResource.getInputStream()) {
            assertThat(is.read()).isEqualTo(0x50);
            assertThat(is.read()).isEqualTo(0x4b);
            assertThat(is.read()).isEqualTo(0x03);
            assertThat(is.read()).isEqualTo(0x04);
        }
    }

}
