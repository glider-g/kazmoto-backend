package kz.kazmoto.rest.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kz.kazmoto.glob.ejb.AppEjb;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.UnAuthCodeException;
import kz.kazmoto.org.RestSecurityContext;
import kz.kazmoto.org.ejb.UserEjb;
import kz.kazmoto.org.model.User;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private static final String AUTH_SCHEME = "JWT";

    @EJB private UserEjb userEjb;
    @EJB private AppEjb appEjb;

    private boolean ignorePath(ContainerRequestContext requestContext){
        String path = requestContext.getUriInfo().getPath();
        switch (path){
            case "/org/users/login":
            case "/org/users/registration":
                return true;
            default:
                return false;
        }
    }

    private User validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(appEjb.getSecretKey());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (JWTDecodeException e) {
            throw new UnAuthCodeException("Token not correct");
        }
        String userId = decodedJWT.getSubject();
        if (userId == null)
            throw new UnAuthCodeException("Token payload does not contain key \"sub\"");

        User user = userEjb.findById(Long.valueOf(decodedJWT.getSubject()));
        if (user == null) throw new UnAuthCodeException("User not found");
        return user;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (ignorePath(requestContext)){
            return;
        }

        // Get the Authorization header from the request
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // Validate the Authorization header
        if (authHeader == null || !authHeader.toLowerCase().startsWith(AUTH_SCHEME.toLowerCase() + " ")) {
            throw new BadRequestCodeException("Token format error");
        }
        // Extract the token from the Authorization header
        String token = authHeader.substring(AUTH_SCHEME.length()).trim();

        // Validate the token
        User user = validateToken(token);
        requestContext.setSecurityContext(new RestSecurityContext(user));
    }
}