package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.opr.excess.ejb.ExcessEjb;
import kz.kazmoto.opr.excess.ejb.ExcessProductEjb;
import kz.kazmoto.opr.excess.model.ExcessProduct;
import kz.kazmoto.rest.serializer.opr.ExcessProductSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("opr/excess-products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExcessProductRest {

    @EJB private ExcessProductEjb excessProductEjb;
    @EJB private ExcessEjb excessEjb;
    @EJB private ProductEjb productEjb;

    private ExcessProductSer excessProductSer = new ExcessProductSer();

    @GET
    public Response list(
            @QueryParam("excessId") Long excessId) {
        if (excessId == null) throw new BadRequestCodeException("quarry param 'excessId' required");

        List<ExcessProduct> excessProducts = excessProductEjb.findByExcess(excessId);
        return Response.ok(excessProductSer.convert(excessProducts)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        ExcessProduct excessProduct = new ExcessProduct();
        excessProduct.setExcess(getValue(reqBody, "excess.id", Long.class, id -> excessEjb.findById(id)));
        excessProduct.setProduct(getValue(reqBody, "product.id", Long.class, id -> productEjb.findById(id)));
        excessProduct.setQuantity(getValue(reqBody,"quantity", BigInteger.class));

        ExcessProduct savedExcessProduct = excessProductEjb.create(excessProduct);
        return Response.ok(excessProductSer.convert(savedExcessProduct)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        excessProductEjb.remove(id);
        return MessageResponse.ok("ExcessProduct successfully deleted");
    }
}
