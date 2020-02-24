package com.cgi.eoss.eopp.util;

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
    PRODUCT_ARCHIVE_CHECKSUM("X-Eopp-Product-Archive-Checksum");

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

}
