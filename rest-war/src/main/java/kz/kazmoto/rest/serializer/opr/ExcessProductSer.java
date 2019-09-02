package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.excess.model.ExcessProduct;
import kz.kazmoto.rest.serializer.nom.ProductSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class ExcessProductSer extends Serializer<ExcessProduct> {
    private ProductSer productSer;

    public ExcessProductSer() {
        productSer = new ProductSer();
    }

    @Override
    protected ObjectNode serialize(ExcessProduct excessProduct) {
        ObjectNode node = createObjectNode();
        node.put("id", excessProduct.getId());
        node.set("product", productSer.convert(excessProduct.getProduct()));
        node.put("quantity", excessProduct.getQuantity());
        return node;
    }
}
