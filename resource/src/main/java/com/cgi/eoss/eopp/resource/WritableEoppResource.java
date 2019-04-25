package com.cgi.eoss.eopp.resource;

import org.springframework.core.io.WritableResource;

/**
 * <p>Specification of a {@link EoppResource} which can provide an {@link java.io.OutputStream} to receive data.</p>
 * <p>Intended for use in lazy provisioning.</p>
 */
public interface WritableEoppResource extends EoppResource, WritableResource {
}
