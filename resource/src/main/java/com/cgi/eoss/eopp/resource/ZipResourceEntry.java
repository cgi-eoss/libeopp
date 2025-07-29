package com.cgi.eoss.eopp.resource;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.file.attribute.FileTime;

public record ZipResourceEntry(
        String path,
        FileTime lastModified,
        @Nullable Resource resource
) {
    public ZipResourceEntry(String path, FileTime lastModified) {
        this(path, lastModified, null);
    }

    public ZipResourceEntry(String path, @NonNull Resource resource) {
        this(path, safeGetFileTime(resource), resource);
    }

    private static FileTime safeGetFileTime(Resource resource) {
        try {
            return FileTime.fromMillis(resource.lastModified());
        } catch (IOException ignored) {
            return FileTime.fromMillis(0L);
        }
    }
}
