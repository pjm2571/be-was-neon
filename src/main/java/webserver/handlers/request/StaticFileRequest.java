package webserver.handlers.request;

import java.util.Map;

public class StaticFileRequest extends HttpRequest {

    public StaticFileRequest(String startLine) {
        super(startLine);
    }

}
