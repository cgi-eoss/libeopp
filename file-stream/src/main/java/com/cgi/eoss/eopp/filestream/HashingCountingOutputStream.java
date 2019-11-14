package com.cgi.eoss.eopp.filestream;

import com.cgi.eoss.eopp.file.FileMetas;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.HashingOutputStream;
import com.google.common.io.CountingOutputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>A wrapper for an {@link OutputStream} to provide convenient (post-{@link #close()}) access to the checksum and
 * size of the data transferred.</p>
 */
public class HashingCountingOutputStream extends OutputStream {

    private final HashFunction hashFunction;
    private CountingOutputStream countingOutputStream;
    private HashingOutputStream hashingOutputStream;

    public HashingCountingOutputStream(OutputStream outputStream) {
        this(FileMetas.DEFAULT_HASH_FUNCTION, outputStream);
    }

    public HashingCountingOutputStream(HashFunction hashFunction, OutputStream outputStream) {
        this.hashFunction = hashFunction;
        this.countingOutputStream = new CountingOutputStream(outputStream);
        this.hashingOutputStream = new HashingOutputStream(hashFunction, countingOutputStream);
    }

    /**
     * @see CountingOutputStream#getCount()
     */
    public long getCount() {
        return countingOutputStream.getCount();
    }

    /**
     * @see HashingOutputStream#hash()
     */
    public HashCode hash() {
        return hashingOutputStream.hash();
    }

    /**
     * @see FileMetas#checksum(HashCode, HashFunction)
     */
    public String checksum() {
        return FileMetas.checksum(hash(), hashFunction);
    }

    @Override
    public void write(int b) throws IOException {
        hashingOutputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        hashingOutputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        hashingOutputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        hashingOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        hashingOutputStream.close();
    }

}
