package webserver.handler;

import webserver.handler.url.LoginHandler;
import webserver.handler.url.LogoutHandler;
import webserver.handler.url.MainHandler;
import webserver.handler.url.RegistrationHandler;
import webserver.Url;

public class UrlHandlerFactory {
    public UrlHandler createUrlHandler(Url url) {
        return switch (url) {
            case REGISTRATION -> new RegistrationHandler();
            case LOGIN -> new LoginHandler();
            case MAIN -> new MainHandler();
            case LOGOUT -> new LogoutHandler();
        };
    }
}
