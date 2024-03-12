package webserver;

public enum Route {
    STATIC("src/main/resources/static/"),
    TEMPLATE("src/main/resources/templates/");

    private String route;

    Route(String route){
        this.route = route;
    }

    public String getRoute(String path){
        return route + path;
    }
}
