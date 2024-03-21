package webserver.response;

public abstract class HttpResponse {
    protected String startLine;
    protected String responseHeader;
    protected byte[] responseBody;

    public HttpResponse(String startLine, String responseHeader) {
        this.startLine = startLine;
        this.responseHeader = responseHeader;
    }

    public HttpResponse(String startLine, String responseHeader, byte[] responseBody) {
        this.startLine = startLine;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public String getStartLine() {
        return startLine;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

}
