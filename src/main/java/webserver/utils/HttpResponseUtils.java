package webserver.utils;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.StatusCode;


import java.io.*;
import java.util.stream.Stream;

public class HttpResponseUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String SPACE = " ";
    private static final String CRLF = "\r\n";


}
