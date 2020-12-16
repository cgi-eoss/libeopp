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

package com.cgi.eoss.eopp.resource;

import org.springframework.core.io.WritableResource;

/**
 * <p>Specification of a {@link EoppResource} which can provide an {@link java.io.OutputStream} to receive data.</p>
 * <p>Intended for use in lazy provisioning.</p>
 */
public interface WritableEoppResource extends EoppResource, WritableResource {
}
