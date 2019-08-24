package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.resource.EoppPathResource;
import com.cgi.eoss.eopp.resource.EoppResource;
import com.google.common.collect.ImmutableSet;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Set;

/**
 * <p>{@link Resolver} implementation to find targets on a filesystem.</p>
 *
 * @see EoppPathResource
 */
public class PathResolver implements Resolver {

    private static final Set<String> PROTOCOLS = ImmutableSet.of("file");

    @Override
    public boolean canResolve(URI uri) {
        return PROTOCOLS.contains(uri.getScheme());
    }

    @Override
    public EoppResource resolveUri(URI uri) {
        return new EoppPathResource(Paths.get(uri));
    }

}
