package webserver.handler;

import webserver.handler.url.LoginHandler;
import webserver.handler.url.RegistrationHandler;
import webserver.handler.url.RootHandler;
import webserver.Url;

public class UrlHandlerFactory {
    public UrlHandler createUrlHandler(Url url) {
        return switch (url) {
            case ROOT -> new RootHandler();
            case REGISTRATION -> new RegistrationHandler();
            case LOGIN -> new LoginHandler();
        };
    }
}
