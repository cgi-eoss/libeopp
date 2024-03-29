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

package com.cgi.eoss.eopp.resolver;

import com.cgi.eoss.eopp.resource.EoppPathResource;
import com.cgi.eoss.eopp.resource.EoppResource;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Set;

/**
 * <p>{@link Resolver} implementation to find targets on a filesystem.</p>
 *
 * @see EoppPathResource
 */
public class PathResolver implements Resolver {

    private static final Set<String> PROTOCOLS = Set.of("file");

    @Override
    public boolean canResolve(URI uri) {
        return PROTOCOLS.contains(uri.getScheme());
    }

    @Override
    public EoppResource resolveUri(URI uri) {
        return new EoppPathResource(Paths.get(uri));
    }

}
