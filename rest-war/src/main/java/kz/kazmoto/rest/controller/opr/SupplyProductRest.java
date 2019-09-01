package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.opr.supply.ejb.SupplyEjb;
import kz.kazmoto.opr.supply.ejb.SupplyProductEjb;
import kz.kazmoto.opr.supply.model.SupplyProduct;
import kz.kazmoto.rest.serializer.opr.SupplyProductSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("opr/supply-products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SupplyProductRest {

    @EJB private SupplyProductEjb supplyProductEjb;
    @EJB private SupplyEjb supplyEjb;
    @EJB private ProductEjb productEjb;

    private SupplyProductSer supplyProductSer = new SupplyProductSer();

    @GET
    public Response list(
            @QueryParam("supplyId") Long supplyId) {
        if (supplyId == null) throw new BadRequestCodeException("quarry param 'supplyId' required");

        List<SupplyProduct> supplyProducts = supplyProductEjb.findBySupply(supplyId);
        return Response.ok(supplyProductSer.convert(supplyProducts)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        SupplyProduct supplyProduct = new SupplyProduct();
        supplyProduct.setSupply(getValue(reqBody, "supply.id", Long.class, id -> supplyEjb.findById(id)));
        supplyProduct.setProduct(getValue(reqBody, "product.id", Long.class, id -> productEjb.findById(id)));
        supplyProduct.setPurchasePrice(getValue(reqBody,"purchasePrice", BigDecimal.class));
        supplyProduct.setQuantity(getValue(reqBody,"quantity", BigInteger.class));

        SupplyProduct savedSupplyProduct = supplyProductEjb.create(supplyProduct);
        return Response.ok(supplyProductSer.convert(savedSupplyProduct)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        supplyProductEjb.remove(id);
        return MessageResponse.ok("SupplyProduct successfully deleted");
    }
}
