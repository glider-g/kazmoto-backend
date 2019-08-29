package kz.kazmoto.rest.controller.org;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.ejb.AppEjb;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.exceptions.ValidateCodeException;
import kz.kazmoto.org.ejb.UserEjb;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.serializer.org.UserSer;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;
import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("org/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRest {
    @EJB private UserEjb userEjb;
    @EJB private AppEjb appEjb;

    private UserSer userSer = new UserSer();

    @POST
    @Path("registration")
    public Response registration(JsonNode reqBody) {
        User user = new User();
        user.setUsername(getValue(reqBody,"username", String.class));
        user.setFirstName(getValue(reqBody, "firstName", String.class));
        user.setLastName(getValue(reqBody, "lastName", String.class));
        String password = getValue(reqBody, "password", String.class);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));

        User savedUser = userEjb.create(user);
        return Response.ok(userSer.convert(savedUser)).build();
    }

    @POST
    @Path("login")
    public Response login(JsonNode reqBody) {
        String username = getValue(reqBody,"username", String.class);
        String password = getValue(reqBody,"password", String.class);

        User user = userEjb.findByUsername(username);
        if (user == null) {
            throw new NotFoundCodeException("User with this username does not exist");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new ValidateCodeException("Invalid password");
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 3);

        Algorithm algorithm = Algorithm.HMAC256(appEjb.getSecretKey());
        String token = JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withExpiresAt(c.getTime())
                .sign(algorithm);

        ObjectNode resNode = createObjectNode();
        resNode.set("user", userSer.convert(user));
        resNode.put("token", token);

        return Response.ok(resNode).build();
    }
}
