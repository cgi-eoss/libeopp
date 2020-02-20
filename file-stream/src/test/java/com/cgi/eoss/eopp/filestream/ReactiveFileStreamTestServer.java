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

package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import org.slf4j.Logger;
import org.springframework.core.io.UrlResource;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

public class ReactiveFileStreamTestServer extends ReactorFileStreamTestServerGrpc.FileStreamTestServerImplBase {
    private static final Logger log = getLogger(ReactiveFileStreamTestServer.class);

    @Override
    public Flux<FileChunk> getFile(Mono<GetFileParam> request) {
        return request
                .map(r -> {
                    try {
                        return new UrlResource(URI.create(r.getUri()));
                    } catch (MalformedURLException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .flatMapMany(FileStreams::create);
    }

}
