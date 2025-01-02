package com.cgi.eoss.eopp.resource;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.file.attribute.FileTime;

public final class ZipResourceEntry {
    private final String path;
    private final FileTime lastModified;

    private final Resource resource;

    public ZipResourceEntry(String path, FileTime lastModified) {
        this(path, lastModified, null);
    }

    public ZipResourceEntry(String path, Resource resource) {
        this.path = path;
        this.resource = resource;
        this.lastModified = safeGetFileTime(resource);
    }

    public ZipResourceEntry(String path, FileTime lastModified, @Nullable Resource resource) {
        this.path = path;
        this.lastModified = lastModified;
        this.resource = resource;
    }

    private FileTime safeGetFileTime(Resource resource) {
        try {
            return FileTime.fromMillis(resource.lastModified());
        } catch (IOException ignored) {
            return FileTime.fromMillis(0L);
        }
    }

    public String getPath() {
        return path;
    }

    public FileTime getLastModified() {
        return lastModified;
    }

    @Nullable
    public Resource getResource() {
        return resource;
    }
}
