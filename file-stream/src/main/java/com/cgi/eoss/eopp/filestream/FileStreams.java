package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

/**
 * <p>Utility methods for working with streams of {@link com.cgi.eoss.eopp.file.FileChunk} objects.</p>
 */
public final class FileStreams {

    private FileStreams() {
        // no-op for utility class
    }

    /**
     * <p>Perform a reactive gRPC call to a service returning <code>stream FileChunk</code> , and send the resulting
     * data to the given output stream.</p>
     * <p>Does not close or flush the output stream.</p>
     *
     * @param <P>              The gRPC service parameter (request) message type.
     * @param request          The gRPC service parameter message.
     * @param fileStreamMethod The method on a reactive gRPC stub used to resolve the file stream.
     * @param outputStream     The destination stream for the FileChunk data.
     */
    public static <P> void writeToStream(Mono<P> request, Function<Mono<P>, Flux<FileChunk>> fileStreamMethod, OutputStream outputStream) {
        request.as(fileStreamMethod)
                .subscribe(fileChunk -> {
                    try {
                        fileChunk.getData().writeTo(outputStream);
                    } catch (IOException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

}
