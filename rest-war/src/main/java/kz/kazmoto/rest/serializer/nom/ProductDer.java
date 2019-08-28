package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.nom.ejb.CategoryEjb;
import kz.kazmoto.nom.ejb.DeviceEjb;
import kz.kazmoto.nom.model.Device;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.nom.model.Category;
import kz.kazmoto.rest.utility.jackson.Deserializer;

import java.math.BigDecimal;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


public class ProductDer extends Deserializer<Product> {

    private CategoryEjb categoryEjb;
    private DeviceEjb deviceEjb;

    public ProductDer(CategoryEjb categoryEjb, DeviceEjb deviceEjb) {
        this.categoryEjb = categoryEjb;
        this.deviceEjb = deviceEjb;
    }

    @Override
    protected Product deserialize(JsonNode node) {
        Product product = new Product();

        product.setName(getValue(node,"name", String.class));

        Long categoryId = getValue(node, "category.id", Long.class);
        Category category = categoryEjb.findById(categoryId);
        if (category == null) throw new NotFoundCodeException("Category not found");
        product.setCategory(category);

        Long deviceId = getValue(node, "device.id", Long.class);
        Device device = deviceEjb.findById(deviceId);
        if (device == null) throw new NotFoundCodeException("Device not found");
        product.setDevice(device);

        product.setPrice(getValue(node,"price", BigDecimal.class));

        product.setPurchasePrice(getValue(node,"purchasePrice", BigDecimal.class));

        return product;
    }
}
