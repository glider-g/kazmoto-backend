package kz.kazmoto.rest.serializer.org;


import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.ValidateCodeException;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.utility.jackson.Deserializer;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

public class UserRegisterDer extends Deserializer<User> {
    @Override
    protected User deserialize(JsonNode node) {
        User user = new User();
        user.setUsername(getValue(node,"username", String.class));
        user.setPassword(getValue(node, "password", String.class));
        user.setFirstName(getValue(node, "firstName", String.class));
        user.setLastName(getValue(node, "lastName", String.class));
        return user;
    }

    @Override
    protected void validate(User user) {
        if (user.getPassword().length()<8) {
            throw new ValidateCodeException("Password must be at least 8 character");
        }
    }
}
