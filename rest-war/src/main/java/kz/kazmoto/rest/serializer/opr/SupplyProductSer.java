package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.supply.model.SupplyProduct;
import kz.kazmoto.rest.serializer.nom.ProductSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class SupplyProductSer extends Serializer<SupplyProduct> {
    private ProductSer productSer;

    public SupplyProductSer() {
        productSer = new ProductSer();
    }

    @Override
    protected ObjectNode serialize(SupplyProduct supplyProduct) {
        ObjectNode node = createObjectNode();
        node.put("id", supplyProduct.getId());
        node.set("product", productSer.convert(supplyProduct.getProduct()));
        node.put("purchasePrice", supplyProduct.getPurchasePrice());
        node.put("quantity", supplyProduct.getQuantity());
        return node;
    }
}
