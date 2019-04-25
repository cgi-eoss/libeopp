package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * <p>Specialisation of {@link Resource} for resources associated with {@link FileMeta}.</p>
 */
public interface EoppResource extends Resource {

    /**
     * @return The libeopp metadata associated with the data referenced by this resource.
     */
    FileMeta getFileMeta();

    /**
     * @return True if this resource should be treated as cacheable by anyone resolving it.
     */
    boolean isCacheable();

    /**
     * <p>Specifies whether the resolver's data retrieval methods should be retried, given that a specified throwable
     * has been thrown.</p>
     *
     * @param throwable The throwable that caused the initial attempt to fail
     * @return {@code true} if the data retrieval method should be retried
     */
    default boolean shouldRetry(Throwable throwable) {
        return false;
    }

    @Override
    default long contentLength() throws IOException {
        return getFileMeta().getSize();
    }

}
