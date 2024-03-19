package webserver.type;

public enum ContentType {
    html("text/html;charset=utf-8"),
    css("text/css;charset=utf-8"),
    js("application/javascript"),
    svg("image/svg+xml"),
    ico("image/x-icon"),
    png("image/png"),
    jpg("image/jpeg"),
    jpeg("image/jpeg");

    private final String mimeType;

    ContentType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
