package webserver.response;

public abstract class HttpResponse {
    protected String startLine;
    protected String requestHeader;
    protected byte[] requestBody;

    public HttpResponse(String startLine, String requestHeader) {
        this.startLine = startLine;
        this.requestHeader = requestHeader;
    }

    public HttpResponse(String startLine, String requestHeader, byte[] requestBody) {
        this.startLine = startLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public String getStartLine() {
        return startLine;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public byte[] getRequestBody() {
        return requestBody;
    }

}
