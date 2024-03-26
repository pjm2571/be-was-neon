package webserver.handler;

import webserver.handler.url.LoginHandler;
import webserver.handler.url.RegistrationHandler;
import webserver.Url;

public class UrlHandlerFactory {
    public UrlHandler createUrlHandler(Url url) {
        return switch (url) {
            case REGISTRATION -> new RegistrationHandler();
            case LOGIN -> new LoginHandler();
        };
    }
}
