package kz.kazmoto.org;

import kz.kazmoto.org.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class RestSecurityContext implements SecurityContext {
    private User user;

    public RestSecurityContext(User user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {return this.user;}

    @Override
    public boolean isUserInRole(String s) {
        return s.length()>5;
    }

    @Override
    public boolean isSecure() {return true;}

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}
