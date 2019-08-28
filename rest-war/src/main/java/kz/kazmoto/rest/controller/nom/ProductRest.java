package kz.kazmoto.rest.controller.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.nom.ejb.CategoryEjb;
import kz.kazmoto.nom.ejb.DeviceEjb;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.nom.model.Device;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.rest.serializer.nom.ProductDer;
import kz.kazmoto.rest.serializer.nom.ProductSer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("nom/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRest {
    @EJB private ProductEjb productEjb;
    @EJB private CategoryEjb categoryEjb;
    @EJB private DeviceEjb deviceEjb;

    private ProductSer productSer = new ProductSer();
    private ProductDer productDer;

    @PostConstruct
    public void init() {
        productDer = new ProductDer(categoryEjb, deviceEjb);
    }

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
    public Response create(JsonNode bodyNode) {
        Product product = productDer.convert(bodyNode);

        Product savedProduct = productEjb.create(product);
        return Response.ok(productSer.convert(savedProduct)).build();
    }

    @POST
    @Path("{id}")
    public Response update(@PathParam("id") Long id, JsonNode bodyNode) {
        Product product = productEjb.findById(id);
        if (product == null) {
            throw new NotFoundCodeException("Product with this id not exist");
        }
        Product updatedProduct = productDer.convert(bodyNode);

        product.setName(updatedProduct.getName());
        product.setCategory(updatedProduct.getCategory());
        product.setDevice(updatedProduct.getDevice());
        product.setPrice(updatedProduct.getPrice());
        product.setPurchasePrice(updatedProduct.getPurchasePrice());

        Product savedProduct = productEjb.update(product);
        return Response.ok(productSer.convert(savedProduct)).build();
    }
}
