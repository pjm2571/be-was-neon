package webserver.handler.inputhandler;

import webserver.http.request.HttpRequest;

import java.io.*;

public class ArticleInputHandler {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String BOUNDARY = "boundary=";
    private static final String BOUNDARY_DELIMITER = "--";

    private String boundary;

    public ArticleInputHandler(HttpRequest httpRequest) {
        // 1) body [0] 은 content!
        // 2) body [1] 은 image!
        this.boundary = getBoundary(httpRequest);
        String[] bodies = getBodies(httpRequest);
        String articleContent = getArticleContent(bodies[0]);
        String articleImgPath = getArticleImgPath(bodies[1]);
    }

    private String getBoundary(HttpRequest httpRequest) {
        String contentType = httpRequest.getHeaderValue(CONTENT_TYPE);
        int startIndex = contentType.indexOf(BOUNDARY) + BOUNDARY.length();
        return BOUNDARY_DELIMITER + contentType.substring(startIndex);
    }

    private String[] getBodies(HttpRequest httpRequest) {
        String[] bodies = httpRequest.getBody().split(boundary);
        return new String[]{bodies[1], bodies[2]};
    }

    private String getArticleContent(String articleBody) {
        String[] tokens = articleBody.split("\n");
        System.out.println("article");
        for (String conent : tokens) {
            System.out.println(conent);
        }
        System.out.println("--");
        return "";
    }

    private String getArticleImgPath(String imgBody) {
        String[] tokens = imgBody.split("\n");
        System.out.println("article");
        for (String conent : tokens) {
            System.out.println(conent);
        }
        System.out.println("--");
        return "";
    }
}
