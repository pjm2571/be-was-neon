package webserver.handler.inputhandler;

public class Article {
    private String userId;
    private String content;
    private String img_path;

    public Article(String userId, String content, String img_path) {
        this.userId = userId;
        this.content = content;
        this.img_path = img_path;
    }

    @Override
    public String toString() {
        return "userID : " + userId + ", content : " + content + ", img_path : " + img_path;
    }
}
