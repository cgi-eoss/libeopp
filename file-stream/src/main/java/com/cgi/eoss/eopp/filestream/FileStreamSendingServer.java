package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.util.Lazy;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class FileStreamSendingServer implements Closeable {
    private static final Logger log = getLogger(FileStreamSendingServer.class);

    private final StreamObserver<FileChunk> responseObserver;
    private final ServerCallStreamObserver<FileChunk> serverCallStreamObserver;
    private final int chunkSize;

    // Lazy fields, instantiated on demand
    private Supplier<FileMeta> fileMeta = Lazy.lazily(this::buildFileMeta);
    private Supplier<ReadableByteChannel> byteChannel = Lazy.lazily(this::buildByteChannel);

    // Convenience fields for various implementations/introspection
    private long transferredBytes = 0;
    private CompletableFuture<?> channelSupplyingFuture = CompletableFuture.completedFuture(null);

    protected FileStreamSendingServer(StreamObserver<FileChunk> responseObserver) {
        this(responseObserver, 8192);
    }

    protected FileStreamSendingServer(StreamObserver<FileChunk> responseObserver, int chunkSize) {
        this.responseObserver = responseObserver;
        this.serverCallStreamObserver = (ServerCallStreamObserver<FileChunk>) responseObserver;
        this.chunkSize = chunkSize;

        serverCallStreamObserver.disableAutoInboundFlowControl();
        final AtomicBoolean wasReady = new AtomicBoolean(false);
        serverCallStreamObserver.setOnReadyHandler(() -> {
            if (serverCallStreamObserver.isReady() && wasReady.compareAndSet(false, true)) {
                log.debug("serverCallStreamObserver onReady");
                serverCallStreamObserver.request(1);
            }
        });
    }

    public void streamFile() throws IOException, InterruptedException {
        try {
            // Messages carry the data in chunks of the specified buffer size
            ReadableByteChannel channel = getByteChannel();
            ByteBuffer buffer = ByteBuffer.allocate(chunkSize);
            int position = 0;

            while (channel.read(buffer) > 0 || !channelSupplyingFuture.isDone()) {
                // Block until the server says another message can be buffered
                while (!serverCallStreamObserver.isReady()) {
                    if (serverCallStreamObserver.isCancelled()) {
                        throw new CancellationException("Client closed FileStream");
                    }
                    Thread.sleep(1);
                }

                log.trace("Sending file chunk from position {}", position);
                int size = buffer.position();
                buffer.rewind();
                responseObserver.onNext(FileChunk.newBuilder()
                        .setPosition(position)
                        .setData(ByteString.copyFrom(buffer, size))
                        .build());
                transferredBytes += size;
                position += buffer.position();
                buffer.flip();
            }

            if (channelSupplyingFuture.isCompletedExceptionally()) {
                channelSupplyingFuture.get(); // Force a throw
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.UNKNOWN
                    .withDescription(e.getMessage())
                    .augmentDescription("streamFile()")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void close() throws IOException {
        getByteChannel().close();
    }

    public FileMeta getFileMeta() {
        return fileMeta.get();
    }

    public ReadableByteChannel getByteChannel() {
        return byteChannel.get();
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public long getTransferredBytes() {
        return transferredBytes;
    }

    protected void setChannelSupplyingFuture(CompletableFuture<?> future) {
        this.channelSupplyingFuture = future;
    }

    protected abstract FileMeta buildFileMeta();

    protected abstract ReadableByteChannel buildByteChannel();

}