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

package com.cgi.eoss.eopp.util;

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
