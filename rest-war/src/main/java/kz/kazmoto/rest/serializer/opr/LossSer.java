package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.loss.model.Loss;
import kz.kazmoto.rest.serializer.org.UserSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class LossSer extends Serializer<Loss> {
    private UserSer userSer;

    public LossSer() {
        userSer = new UserSer();
    }

    @Override
    protected ObjectNode serialize(Loss loss) {
        ObjectNode node = createObjectNode();
        node.put("id", loss.getId());
        node.set("manager", userSer.convert(loss.getManager()));
        node.put("comment", loss.getComment());
        node.put("active", loss.isActive());
        return node;
    }
}
