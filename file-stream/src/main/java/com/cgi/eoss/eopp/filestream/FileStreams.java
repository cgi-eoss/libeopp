package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import com.google.protobuf.ByteString;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.unit.DataSize;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * <p>Utility methods for working with streams of {@link com.cgi.eoss.eopp.file.FileChunk} objects.</p>
 */
public final class FileStreams {

    public static final DataBufferFactory DEFAULT_DATA_BUFFER_FACTORY = new DefaultDataBufferFactory(true);

    private static final int DEFAULT_BUFFER_SIZE = Math.toIntExact(DataSize.ofKilobytes(32).toBytes());

    private FileStreams() {
        // no-op for utility class
    }

    /**
     * <p>Perform a reactive gRPC call to a service returning <code>stream FileChunk</code>, and send the resulting
     * data to the given output stream.</p>
     * <p>Does not close or flush the output stream.</p>
     *
     * @param <P>              The gRPC service parameter (request) message type.
     * @param request          The gRPC service parameter message.
     * @param fileStreamMethod The method on a reactive gRPC stub used to resolve the file stream.
     * @param outputStream     The destination stream for the FileChunk data.
     */
    public static <P> void writeToStream(Mono<P> request, Function<Mono<P>, Flux<FileChunk>> fileStreamMethod, OutputStream outputStream) {
        writeToStream(request.as(fileStreamMethod), outputStream);
    }

    /**
     * <p>Write the given {@link Flux} of FileChunks to the given output stream.</p>
     * <p>Does not close or flush the output stream.</p>
     *
     * @param fileChunkFlux The reactive stream of FileChunk messages.
     * @param outputStream  The destination stream for the FileChunk data.
     */
    public static void writeToStream(Flux<FileChunk> fileChunkFlux, OutputStream outputStream) {
        DataBufferUtils.write(bufferStream(fileChunkFlux), outputStream)
                .doOnNext(DataBufferUtils.releaseConsumer())
                .then().block();
    }

    /**
     * <p>Map the given stream of FileChunk messages into a {@link Flux} of {@link DataBuffer}s.</p>
     * <p>This may be handled manually or consumed with {@link DataBufferUtils}.</p>
     *
     * @param fileChunkFlux The data source as a stream of FileChunk messages.
     * @return A {@link Flux}&lt;{@link DataBuffer}&gt; mapping of the FileChunk stream.
     */
    public static Flux<DataBuffer> bufferStream(Flux<FileChunk> fileChunkFlux) {
        return fileChunkFlux
                .map(chunk -> DEFAULT_DATA_BUFFER_FACTORY.wrap(chunk.getData().asReadOnlyByteBuffer()));
    }

    /**
     * @return A Flux of {@link FileChunk}s from the given {@link Resource}, using the default buffer/message size.
     */
    public static Flux<FileChunk> create(Resource resource) {
        return create(resource, DEFAULT_BUFFER_SIZE);
    }

    /**
     * @return A Flux of {@link FileChunk}s from the given {@link Resource}, using the given buffer/message size.
     */
    public static Flux<FileChunk> create(Resource resource, int bufferSize) {
        AtomicLong position = new AtomicLong(0);
        return DataBufferUtils.read(resource, DEFAULT_DATA_BUFFER_FACTORY, bufferSize)
                .map(db -> FileChunk.newBuilder()
                        .setData(ByteString.copyFrom(db.asByteBuffer()))
                        .setPosition(position.getAndAdd(db.readableByteCount()))
                        .build());
    }

}
