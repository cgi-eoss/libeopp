package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.resource.EoppResource;

import java.io.IOException;
import java.net.URI;

/**
 * <p>General contract for a function which resolves a URI into an {@link EoppResource}.</p>
 * <p>Implementations of this may provide functionality for different protocols or environments.</p>
 */
public interface Resolver {

    /**
     * @param uri The URI to be tested.
     * @return True if this resolver is able to process the given URI.
     */
    boolean canResolve(URI uri);

    /**
     * <p>Resolve the given URI into a GaiaScope resource</p>
     *
     * @param uri The URI to be retrieved
     * @return An object implementing {@link EoppResource}
     */
    EoppResource resolveUri(URI uri) throws IOException;

    /**
     * <p>Find the priority with which this resolver should be used for the given URI.</p>
     *
     * @param uri The URI to be retrieved
     * @return The priority this resolver assigns to itself for resolving the URI
     */
    default int getPriority(URI uri) {
        return getResolverTypePriority();
    }

    /**
     * <p>The default application-wide priority of this resolver type, for coarse-grained prioritisation.</p>
     *
     * @return The configured resolver type priority.
     */
    default int getResolverTypePriority() {
        return 0;
    }

}
