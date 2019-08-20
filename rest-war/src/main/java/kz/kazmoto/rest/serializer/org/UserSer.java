package kz.kazmoto.rest.serializer.org;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.utility.jackson.Serializer;

public class UserSer extends Serializer<User> {
    @Override
    protected ObjectNode serialize(User user) {
        ObjectNode node = createObjectNode();
        node.put("id", user.getId());
        node.put("username", user.getUsername());
        node.put("firstName", user.getFirstName());
        node.put("lastName", user.getLastName());
        return node;
    }
}
