package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.model.Sale;
import kz.kazmoto.rest.serializer.org.UserSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class SaleSer extends Serializer<Sale> {
    private UserSer userSer;

    public SaleSer() {
        userSer = new UserSer();
    }

    @Override
    protected ObjectNode serialize(Sale sale) {
        ObjectNode node = createObjectNode();
        node.put("id", sale.getId());
        node.set("manager", userSer.convert(sale.getManager()));
        node.put("comment", sale.getComment());
        node.put("type", saleTypeToString(sale.getType()));
        node.put("active", sale.isActive());
        node.put("customer", sale.getCustomer());
        return node;
    }

    private String saleTypeToString(Sale.Type saleType){
        switch (saleType){
            case RETAIL:return "retail";
            case WHOLESALE:return "wholesale";
            default: throw new IllegalStateException("can't convert saleType"+saleType+" to string");
        }
    }
}
