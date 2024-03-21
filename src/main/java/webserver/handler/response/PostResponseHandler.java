package webserver.handler.response;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.Config;
import webserver.handler.response.query.LoginHandler;
import webserver.handler.response.query.RegisterHandler;
import webserver.request.HttpRequest;
import webserver.request.PostRequest;
import webserver.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostResponseHandler extends ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostResponseHandler.class);

    private static final String CREATE_USER = "/createUser";
    private static final String LOGIN_USER = "/loginUser";


    public PostResponseHandler(DataOutputStream responseWriter, Config config, HttpRequest httpRequest) {
        super(responseWriter, config, httpRequest);
    }

    @Override
    public void handleResponse() {
        String requestTarget = httpRequest.getRequestTarget();
        String requestBody = ((PostRequest) httpRequest).getRequestBody();


        if (requestTarget.startsWith(CREATE_USER)) {
            RegisterHandler registerHandler = new RegisterHandler(requestBody);
            HttpResponse httpResponse = registerHandler.getResponse();
            writeResponse(httpResponse);
        }

        if (requestTarget.startsWith(LOGIN_USER)) {
            LoginHandler loginHandler = new LoginHandler(requestBody);
            HttpResponse httpResponse = loginHandler.getResponse();
            writeResponse(httpResponse);
        }
    }

    @Override
    protected void writeResponse(HttpResponse httpResponse) {
        try {
            responseWriter.writeBytes(httpResponse.getStartLine());
            responseWriter.writeBytes(httpResponse.getResponseHeader());

            responseWriter.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
