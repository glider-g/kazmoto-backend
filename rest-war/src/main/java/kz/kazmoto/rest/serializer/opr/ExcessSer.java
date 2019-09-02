package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.excess.model.Excess;
import kz.kazmoto.rest.serializer.org.UserSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class ExcessSer extends Serializer<Excess> {
    private UserSer userSer;

    public ExcessSer() {
        userSer = new UserSer();
    }

    @Override
    protected ObjectNode serialize(Excess excess) {
        ObjectNode node = createObjectNode();
        node.put("id", excess.getId());
        node.set("manager", userSer.convert(excess.getManager()));
        node.put("comment", excess.getComment());
        node.put("active", excess.isActive());
        return node;
    }
}
