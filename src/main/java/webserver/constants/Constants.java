package webserver.constants;

public class Constants {
    public static final String SPACE = " ";
    public static final String CRLF = "\r\n";
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String ROOT_URL = "/";
    public static final String DEFAULT_FILE = "index.html";

    public static final String LOGIN_SUCCESS_URL = "/main/" + DEFAULT_FILE;
    public static final String LOGIN_FAIL_URL = "/user/login_failed/ " + DEFAULT_FILE;
    public static final String SLASH = "/";
}
