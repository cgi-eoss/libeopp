package com.cgi.eoss.eopp.file;

import java.io.IOException;

public class EoppFileException extends RuntimeException {
    EoppFileException(IOException cause) {
        super(cause);
    }
}
