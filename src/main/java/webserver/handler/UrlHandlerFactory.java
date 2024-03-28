package webserver.handler;

import webserver.handler.urlhandler.LoginHandler;
import webserver.handler.urlhandler.LogoutHandler;
import webserver.handler.urlhandler.MainHandler;
import webserver.handler.urlhandler.RegistrationHandler;
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
