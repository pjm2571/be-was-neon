package webserver;

public enum ContentType {
    html("Content-Type: text/html;charset=utf-8\r\n"),
    css("Content-Type: text/css;charset=utf-8\r\n"),
    js("Content-Type: application/javascript\r\n"),
    svg("Content-Type: image/svg+xml\r\n"),
    ico("Content-Type: image/x-icon\r\n"),
    png("Content-Type: image/png\r\n"),
    jpg("Content-Type: image/jpeg\r\n"),
    jpeg("Content-Type: image/jpeg\r\n");
    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static String getContentType(String inputType) {
        return valueOf(inputType).type;
    }
}
