package webserver.response;

public class StaticFileResponse extends HttpResponse {

    public StaticFileResponse(String startLine, String responseHeader, byte[] responseBody) {
        super(startLine, responseHeader, responseBody);
    }
}
