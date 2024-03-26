package webserver;

public enum Url {
    LOGIN("/login"),
    REGISTRATION("/registration");
    private final String urlPath;

    Url(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlPath() {
        return urlPath;
    }


}
