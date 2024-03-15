package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private DataOutputStream out;
    private Response response;

    ResponseHandler(OutputStream out, Response response) {
        this.out = new DataOutputStream(out);
        this.response = response;
    }

    public void httpResponse() {
        try {
            out.writeBytes(response.getResponseHeader());
            writeResponseBody();
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeResponseBody() throws IOException {
        if (response.getResponseBody() == null) {
            return;
        }
        out.write(response.getResponseBody(), 0, response.getResponseBody().length);
    }

    public void fileResponse(String filePath) {
        //...파일로도 저장할 경우가 있지 않을까??
    }

}
