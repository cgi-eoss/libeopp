package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * <p>{@link EoppResource} implementation corresponding to target on a filesystem.</p>
 *
 * @see FileSystemResource
 */
public class EoppPathResource extends FileSystemResource implements WritableEoppResource {

    private final FileMeta fileMeta;

    private final Path fsPath;

    public EoppPathResource(Path path) {
        this(path, FileMetas.get(path));
    }

    public EoppPathResource(Path path, String fileName) {
        this(path, FileMeta.newBuilder(FileMetas.get(path)).setFilename(fileName).build());
    }

    public EoppPathResource(Path path, FileMeta fileMeta) {
        super(path);
        this.fileMeta = fileMeta;
        this.fsPath = path;
    }

    @Override
    public FileMeta getFileMeta() {
        return fileMeta;
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean isFile() {
        // This should comply with Path#toFile
        return fsPath.getFileSystem() == FileSystems.getDefault();
    }

    /**
     * @return The {@link Path} referred to by this resource.
     */
    public Path getFsPath() {
        return fsPath;
    }

}
