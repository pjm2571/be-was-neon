package webserver;

public enum ROUTE {
    STATIC("src/main/resources/static/"),
    IMG(STATIC+"img/"),
    COMMENT(STATIC+"comment/"),
    ARTICLE(STATIC + "article/"),
    LOGIN(STATIC + "login/"),
    MAIN(STATIC + "main/"),
    REGISTRATION(STATIC + "registration/");

    private String route;

    ROUTE(String route){
        this.route = route;
    }

    public String getRoute(String path){
        return route + path;
    }
}
