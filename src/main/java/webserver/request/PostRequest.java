package webserver.request;

import webserver.response.HttpResponse;

import java.util.Map;

public class PostRequest extends HttpRequest {
    public PostRequest(String startLine, Map<String, String> headers, String body) {
        super(startLine, headers, body);
    }


    public String getRequestBody() {
        return body;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.get("Content-Length"));
    }

}
