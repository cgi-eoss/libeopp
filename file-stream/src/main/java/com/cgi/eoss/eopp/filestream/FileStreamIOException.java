package com.cgi.eoss.eopp.filestream;

import java.io.IOException;

public class FileStreamIOException extends RuntimeException {
    public FileStreamIOException(Throwable cause) {
        super(cause);
    }

    public FileStreamIOException(String message) {
        super(message);
    }

    public FileStreamIOException(String message, Throwable cause) {
        super(message, cause);
    }
}