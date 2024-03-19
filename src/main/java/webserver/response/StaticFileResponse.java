package webserver.response;

public class StaticFileResponse extends HttpResponse {

    public StaticFileResponse(String requestTarget, String requestHeader, byte[] requestBody) {
        super(requestTarget, requestHeader, requestBody);
    }
}
