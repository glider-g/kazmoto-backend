package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.supply.model.Supply;
import kz.kazmoto.rest.serializer.org.UserSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class SupplySer extends Serializer<Supply> {
    private UserSer userSer;

    public SupplySer() {
        userSer = new UserSer();
    }

    @Override
    protected ObjectNode serialize(Supply supply) {
        ObjectNode node = createObjectNode();
        node.put("id", supply.getId());
        node.set("manager", userSer.convert(supply.getManager()));
        node.put("comment", supply.getComment());
        node.put("active", supply.isActive());
        node.put("supplier", supply.getSupplier());
        return node;
    }
}
