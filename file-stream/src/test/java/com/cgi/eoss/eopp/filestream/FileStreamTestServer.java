package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileChunk;
import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.file.FileMetas;
import com.google.common.base.Stopwatch;
import com.google.common.io.Resources;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStreamTestServer extends FileStreamTestServerGrpc.FileStreamTestServerImplBase {
    private static final Logger log = LoggerFactory.getLogger(FileStreamTestServer.class);

    private final Path targetRoot;

    public FileStreamTestServer(Path targetRoot) {
        this.targetRoot = targetRoot;
    }

    public FileStreamTestServer() {
        this(null);
    }

    @Override
    public void getFile(GetFileParam request, StreamObserver<FileChunk> responseObserver) {
        URLConnection testFileConnection = getTestFileConnection(request.getUri());

        try (FileStreamSendingServer fileStreamServer = new FileStreamSendingServer(responseObserver) {
            @Override
            protected FileMeta buildFileMeta() {
                try {
                    return FileMeta.newBuilder()
                            .setFilename(StringUtils.getFilename(request.getUri()))
                            .setSize(testFileConnection.getContentLength())
                            .setChecksum(FileMetas.checksum(Resources.asByteSource(testFileConnection.getURL())))
                            .build();
                } catch (IOException e) {
                    throw new FileStreamIOException(e);
                }
            }

            @Override
            protected ReadableByteChannel buildByteChannel() {
                try {
                    return Channels.newChannel(testFileConnection.getInputStream());
                } catch (IOException e) {
                    throw new FileStreamIOException(e);
                }
            }
        }) {
            log.info("Serving output file: {} ({} bytes)", fileStreamServer.getFileMeta().getFilename(), fileStreamServer.getFileMeta().getSize());
            Stopwatch stopwatch = Stopwatch.createStarted();
            fileStreamServer.streamFile();
            log.info("Served output file {} ({} bytes) in {}", fileStreamServer.getFileMeta().getFilename(), fileStreamServer.getFileMeta().getSize(), stopwatch.stop().elapsed());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static URLConnection getTestFileConnection(String uri) {
        try {
            return URI.create(uri).toURL().openConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
