package com.cgi.eoss.eopp.resolver;


import com.cgi.eoss.eopp.resource.EoppResource;

import java.net.URI;
import java.util.Collection;

/**
 * <p>A facade to {@link Resolver} implementations.</p>
 */
public interface ResolverFacade extends Resolver {

    /**
     * <p>Find a {@link EoppResource} for the given URI from the registered backends to this facade.</p>
     * <p>The returned resource has been checked for {@link EoppResource#isReadable()}.</p>
     *
     * @param uri The URI to resolve.
     * @return A resource which should be able to provide an InputStream for the data referenced by the URI.
     */
    @Override
    EoppResource resolveUri(URI uri);

    /**
     * <p>Register the given resolver with this facade.</p>
     *
     * @param resolver The resolver to be registered.
     */
    void registerResolver(Resolver resolver);

    /**
     * <p>Remove the given resolver from the available backends for this fabade.</p>
     *
     * @param resolver The resolver to be unregistered.
     */
    void unregisterResolver(Resolver resolver);

    /**
     * <p>Replace all currently-registered resolvers with the given collection.</p>
     *
     * @param newResolvers The new resolvers to be registered.
     */
    void replaceResolvers(Collection<Resolver> newResolvers);

}
