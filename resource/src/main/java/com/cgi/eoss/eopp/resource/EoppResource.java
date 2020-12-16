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
