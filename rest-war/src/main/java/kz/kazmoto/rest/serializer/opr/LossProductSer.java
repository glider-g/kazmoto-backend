package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.loss.model.LossProduct;
import kz.kazmoto.rest.serializer.nom.ProductSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class LossProductSer extends Serializer<LossProduct> {
    private ProductSer productSer;

    public LossProductSer() {
        productSer = new ProductSer();
    }

    @Override
    protected ObjectNode serialize(LossProduct lossProduct) {
        ObjectNode node = createObjectNode();
        node.put("id", lossProduct.getId());
        node.set("product", productSer.convert(lossProduct.getProduct()));
        node.put("quantity", lossProduct.getQuantity());
        return node;
    }
}
