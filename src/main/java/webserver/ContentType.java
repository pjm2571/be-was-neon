package webserver;

public enum ContentType {
    html("Content-Type: text/html;charset=utf-8\r\n"),
    css("Content-Type: text/css;charset=utf-8\r\n"),
    svg("Content-Type: image/svg+xml\r\n"),
    ico("Content-Type: image/x-icon\r\n");
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static String getContentType(String inputType) {
        return valueOf(inputType).type;
    }
}
