package kz.kazmoto.rest.serializer.opr;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.opr.model.SaleProduct;
import kz.kazmoto.rest.serializer.nom.ProductSer;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class SaleProductSer extends Serializer<SaleProduct> {
    private ProductSer productSer;

    public SaleProductSer() {
        productSer = new ProductSer();
    }

    @Override
    protected ObjectNode serialize(SaleProduct saleProduct) {
        ObjectNode node = createObjectNode();
        node.put("id", saleProduct.getId());
        node.set("product", productSer.convert(saleProduct.getProduct()));
        node.put("price", saleProduct.getPrice());
        node.put("quantity", saleProduct.getQuantity());
        return node;
    }
}
