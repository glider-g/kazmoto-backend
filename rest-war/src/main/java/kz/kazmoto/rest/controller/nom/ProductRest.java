package kz.kazmoto.rest.controller.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.nom.ejb.CategoryEjb;
import kz.kazmoto.nom.ejb.DeviceEjb;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.rest.serializer.nom.ProductSer;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("nom/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRest {
    @EJB private ProductEjb productEjb;
    @EJB private CategoryEjb categoryEjb;
    @EJB private DeviceEjb deviceEjb;

    private ProductSer productSer = new ProductSer();

    @GET
    public Response list(
            @QueryParam("name") @DefaultValue("") String model,
            @QueryParam("categoryName") @DefaultValue("") String name,
            @QueryParam("barcode") @DefaultValue("") String barcode,
            @QueryParam("categoryId") Long categoryId,
            @QueryParam("deviceId") Long deviceId) {
        List<Product> products = productEjb.findByFilter(model, name, barcode, categoryId, deviceId);
        return Response.ok(productSer.convert(products)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Product product = productEjb.findById(id);
        if (product == null) {
            throw new NotFoundCodeException("Product with this id not exist");
        }
        return Response.ok(productSer.convert(product)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        Product product = new Product();

        product.setName(getValue(reqBody,"name", String.class));
        product.setCategory(getValue(reqBody, "category.id", Long.class, id -> categoryEjb.findById(id)));
        product.setDevice(getValue(reqBody, "device.id", Long.class, id -> deviceEjb.findById(id)));
        product.setPrice(getValue(reqBody,"price", BigDecimal.class));
        product.setPurchasePrice(getValue(reqBody,"purchasePrice", BigDecimal.class));

        Product savedProduct = productEjb.create(product);
        return Response.ok(productSer.convert(savedProduct)).build();
    }

    @POST
    @Path("{id}")
    public Response update(@PathParam("id") Long id, JsonNode reqBody) {
        Product product = productEjb.findById(id);
        if (product == null) {
            throw new NotFoundCodeException("Product with this id not exist");
        }

        product.setName(getValue(reqBody,"name", String.class));
        product.setCategory(getValue(reqBody, "category.id", Long.class, id_ -> categoryEjb.findById(id_)));
        product.setDevice(getValue(reqBody, "device.id", Long.class ,id_ -> deviceEjb.findById(id_)));
        product.setPrice(getValue(reqBody,"price", BigDecimal.class));
        product.setPurchasePrice(getValue(reqBody,"purchasePrice", BigDecimal.class));

        Product savedProduct = productEjb.update(product);
        return Response.ok(productSer.convert(savedProduct)).build();
    }
}
