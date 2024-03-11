package webserver;

public enum CONTENT_TYPE {
    html("text/html;charset=utf-8"),
    css("text/css;charset=utf-8"),
    svg("image/svg+xml"),
    ico("image/x-icon");

    private static final String CONTENT = "Content-Type: ";
    private static final String NEW_LINE = "\r\n";
    private final String type;

    CONTENT_TYPE(String type) {
        this.type = type;
    }

    public static String getContentType(String inputType) {
        return CONTENT + valueOf(inputType).type + NEW_LINE;
    }
}
