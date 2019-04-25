package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.resource.EoppResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DefaultResolverFacade implements ResolverFacade {
    private static final Logger log = LoggerFactory.getLogger(DefaultResolverFacade.class);

    private final Set<Resolver> resolvers = new HashSet<>();

    @Override
    public boolean canResolve(URI uri) {
        return getAvailableResolvers(uri).findAny().isPresent();
    }

    @Override
    public EoppResource resolveUri(URI uri) {
        List<Resolver> availableResolvers = getAvailableResolvers(uri)
                .sorted(Comparator.comparingInt(o -> o.getPriority(uri)))
                .collect(toList());

        for (Resolver resolver : availableResolvers) {
            try {
                log.debug("Attempting resolution with {} for uri {}", resolver, uri);
                EoppResource resource = resolver.resolveUri(uri);
                if (resource.isReadable()) {
                    return resource;
                } else {
                    log.debug("Resolver {} does not recognise resource as readable for uri {}", resolver, uri);
                }
            } catch (Exception e) {
                log.error("Failed to resolve with {} for uri {}", resolver, uri, e);
            }
        }
        throw new UnsupportedOperationException("No resolver was able to process the URI: " + uri);
    }

    @Override
    public void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    @Override
    public void unregisterResolver(Resolver resolver) {
        resolvers.remove(resolver);
    }

    @Override
    public void replaceResolvers(Collection<Resolver> newResolvers) {
        resolvers.clear();
        resolvers.addAll(newResolvers);
    }

    private Stream<Resolver> getAvailableResolvers(URI uri) {
        return resolvers.stream()
                .filter(r -> r.canResolve(uri));
    }

}
