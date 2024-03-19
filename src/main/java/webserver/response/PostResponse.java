package webserver.response;

public class PostResponse extends HttpResponse {

    public PostResponse(String requestTarget, String requestBody) {
        super(requestTarget);
        this.requestBody = requestBody;
    }


}
