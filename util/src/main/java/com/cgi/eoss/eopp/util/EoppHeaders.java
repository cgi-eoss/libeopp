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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Common HTTP headers for working with libeopp-based applications.</p>
 */
public enum EoppHeaders {

    /**
     * <p>Boolean header to request that a server calculates full {@link com.cgi.eoss.eopp.file.FileMeta} attributes
     * for the response.</p>
     * <p>Typical 'truthy' values: <code>?1</code>, <code>1</code>, <code>true</code>.</p>
     */
    COMPUTE_ARCHIVE_ATTRS("X-Eopp-Compute-Archive-Attrs"),

    /**
     * <p>The total, absolute size of a product being requested. For example, this may indicate the uncompressed size,
     * when the server is returning a compressed archive.</p>
     */
    PRODUCT_TOTAL_SIZE("X-Eopp-Product-Total-Size"),

    /**
     * <p>The archive name of a product being requested. This may differ from the Content-Disposition filename header,
     * or carry additional information for use by libeopp-based applications.</p>
     */
    PRODUCT_ARCHIVE_NAME("X-Eopp-Product-Archive-Name"),

    /**
     * <p>The actual size of a product being requested. For example, the size of a compressed archive. This should match
     * the Content-Length header.</p>
     */
    PRODUCT_ARCHIVE_SIZE("X-Eopp-Product-Archive-Size"),

    /**
     * <p>A {@link com.cgi.eoss.eopp.file.FileMeta#getChecksum()} string for a product being requested.</p>
     */
    PRODUCT_ARCHIVE_CHECKSUM("X-Eopp-Product-Archive-Checksum"),

    /**
     * <p>A base64-encoded {@link com.cgi.eoss.eopp.file.FileMeta} describing a file being requested.</p>
     * <p>If multiple {@link EoppHeaders} are available on the resource, this should be the first source of truth for
     * metadata values.</p>
     */
    FILE_META("X-Eopp-File-Meta");

    // adapted from https://tools.ietf.org/html/rfc2616#section-3.3
    private static final SimpleDateFormat[] HTTP_DATE_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)};

    private final String header;

    EoppHeaders(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    /**
     * <p>Function to extract a filename from a standard HTTP Content-Disposition header.</p>
     */
    public static final Function<String, Optional<String>> FILENAME_FROM_HTTP_HEADER = s -> {
        Matcher matcher = Pattern.compile("attachment; filename=\"?(.*)\"?").matcher(s);
        return matcher.matches() ? Optional.of(matcher.group(1)) : Optional.empty();
    };

    /**
     * <p>Function to parse an Instant from one of the various HTTP date formats available.</p>
     *
     * @see <a href="https://tools.ietf.org/html/rfc2616#section-3.3">https://tools.ietf.org/html/rfc2616#section-3.3</a>
     */
    public static Instant parseInstant(String httpDate) {
        for (SimpleDateFormat format : HTTP_DATE_FORMATS) {
            try {
                return format.parse(httpDate).toInstant();
            } catch (ParseException ignored) {
                // nothing to do
            }
        }
        throw new IllegalArgumentException("Could not parse date as any HTTP-Date format: " + httpDate);
    }

}
