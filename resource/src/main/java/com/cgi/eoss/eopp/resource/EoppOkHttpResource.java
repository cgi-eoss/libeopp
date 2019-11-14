package com.cgi.eoss.eopp.resource;

import com.cgi.eoss.eopp.file.FileMeta;
import com.cgi.eoss.eopp.util.EoppHeaders;
import com.cgi.eoss.eopp.util.Lazy;
import com.cgi.eoss.eopp.util.Timestamps;
import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <p>{@link EoppResource} implementation for retrieving data from HTTP(S) sources.</p>
 */
public class EoppOkHttpResource implements EoppResource {

    private static final Logger log = LoggerFactory.getLogger(EoppOkHttpResource.class);

    private static final SimpleDateFormat[] HTTP_DATE_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)};

    private final OkHttpClient httpClient;
    private final HttpUrl url;
    private final Function<String, Optional<String>> filenameFromHeader = s -> {
        Matcher matcher = Pattern.compile("attachment; filename=\"?(.*)\"?").matcher(s);
        return matcher.matches() ? Optional.of(matcher.group(1)) : Optional.empty();
    };

    private Supplier<HttpResourceMetadata> metadata = Lazy.lazily(this::getHttpResourceMetadata);
    private int remainingRetries = 3;

    public EoppOkHttpResource(OkHttpClient httpClient, HttpUrl url) {
        this.httpClient = httpClient;
        this.url = url;
    }

    @Override
    public FileMeta getFileMeta() {
        FileMeta.Builder fileMeta = FileMeta.newBuilder()
                .setFilename(this.getFilename())
                .setSize(this.contentLength())
                .setLastModified(Timestamps.timestampFromInstant(Instant.ofEpochMilli(this.lastModified())))
                .setExecutable(false);
        metadata.get().checksum.ifPresent(fileMeta::setChecksum);
        return fileMeta.build();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    @Override
    public boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException && remainingRetries > 0) {
            remainingRetries--;
            return true;
        }
        return false;
    }

    @Override
    public boolean exists() {
        return metadata.get().exists;
    }

    @Override
    public boolean isReadable() {
        return metadata.get().readable;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public URL getURL() {
        return url.url();
    }

    @Override
    public URI getURI() {
        return url.uri();
    }

    @Override
    public File getFile() throws IOException {
        throw new FileNotFoundException("HTTP resources may not be resolved as Files");
    }

    @Override
    public long contentLength() {
        return metadata.get().contentLength;
    }

    @Override
    public long lastModified() {
        return metadata.get().lastModified;
    }

    @Override
    public Resource createRelative(String relativePath) {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        return new EoppOkHttpResource(httpClient, url.resolve(relativePath));
    }

    @Override
    public String getFilename() {
        return metadata.get().filename;
    }

    @Override
    public String getDescription() {
        return "HttpUrl [" + url + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            Preconditions.checkState(response.isSuccessful(), "Did not receive successful HTTP response: %s", response);
            return response.body().byteStream();
        } catch (Exception e) {
            Optional.ofNullable(response).ifPresent(Response::close);
            throw e;
        }
    }

    private HttpResourceMetadata getHttpResourceMetadata() {
        // Make a HEAD request to get metadata only
        Request request = new Request.Builder().url(url).head().build();

        HttpResourceMetadata httpResourceMetadata = new HttpResourceMetadata();

        try (Response response = httpClient.newCall(request).execute()) {
            log.debug("Received HEAD response: {}", response);
            httpResourceMetadata.exists = response.isSuccessful();
            httpResourceMetadata.readable = response.isSuccessful();
            httpResourceMetadata.lastModified = Optional.ofNullable(response.header(HttpHeaders.LAST_MODIFIED))
                    .map(h -> parseDate(h).toInstant().toEpochMilli()).orElse(0L);
            httpResourceMetadata.contentLength = Stream.of(Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_SIZE.getHeader())).map(Long::valueOf),
                    Optional.ofNullable(response.header(HttpHeaders.CONTENT_LENGTH)).map(Long::valueOf))
                    .filter(Optional::isPresent).map(Optional::get).findFirst()
                    .orElse(0L);
            httpResourceMetadata.filename = Stream.of(
                    Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_NAME.getHeader())),
                    Optional.ofNullable(response.header(HttpHeaders.CONTENT_DISPOSITION)).flatMap(filenameFromHeader))
                    .filter(Optional::isPresent).map(Optional::get).findFirst()
                    .orElse(StringUtils.getFilename(url.encodedPath()));
            httpResourceMetadata.checksum = Optional.ofNullable(response.header(EoppHeaders.PRODUCT_ARCHIVE_CHECKSUM.getHeader()));
        } catch (IOException e) {
            log.warn("Failed to HEAD resource at {}", url, e);
        }

        return httpResourceMetadata;
    }

    private Date parseDate(String httpDate) {
        for (SimpleDateFormat format : HTTP_DATE_FORMATS) {
            try {
                return format.parse(httpDate);
            } catch (ParseException ignored) {
                // nothing to do
            }
        }
        throw new IllegalArgumentException("Could not parse date as any HTTP-Date format: " + httpDate);
    }

    private static final class HttpResourceMetadata {
        private boolean exists;
        private boolean readable;
        private long lastModified;
        private String filename;
        private long contentLength;
        private Optional<String> checksum;
    }

}
