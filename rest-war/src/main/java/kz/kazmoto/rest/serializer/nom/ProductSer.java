package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class ProductSer extends Serializer<Product> {
    private CategorySer categorySer;

    public ProductSer(){
        categorySer = new CategorySer();
    }

    @Override
    protected ObjectNode serialize(Product product) {
        ObjectNode node = createObjectNode();
        node.put("id", product.getId());
        node.put("name", product.getName());
        node.put("barcode", product.getBarcode());
        node.set("category", categorySer.serialize(product.getCategory()));
        node.put("price", product.getPrice());
        node.put("purchasePrice", product.getPurchasePrice());
        return node;
    }
}
