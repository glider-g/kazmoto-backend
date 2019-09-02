package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.opr.loss.ejb.LossEjb;
import kz.kazmoto.opr.loss.ejb.LossProductEjb;
import kz.kazmoto.opr.loss.model.LossProduct;
import kz.kazmoto.rest.serializer.opr.LossProductSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("opr/loss-products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LossProductRest {

    @EJB private LossProductEjb lossProductEjb;
    @EJB private LossEjb lossEjb;
    @EJB private ProductEjb productEjb;

    private LossProductSer lossProductSer = new LossProductSer();

    @GET
    public Response list(
            @QueryParam("lossId") Long lossId) {
        if (lossId == null) throw new BadRequestCodeException("quarry param 'lossId' required");

        List<LossProduct> lossProducts = lossProductEjb.findByLoss(lossId);
        return Response.ok(lossProductSer.convert(lossProducts)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        LossProduct lossProduct = new LossProduct();
        lossProduct.setLoss(getValue(reqBody, "loss.id", Long.class, id -> lossEjb.findById(id)));
        lossProduct.setProduct(getValue(reqBody, "product.id", Long.class, id -> productEjb.findById(id)));
        lossProduct.setQuantity(getValue(reqBody,"quantity", BigInteger.class));

        LossProduct savedLossProduct = lossProductEjb.create(lossProduct);
        return Response.ok(lossProductSer.convert(savedLossProduct)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        lossProductEjb.remove(id);
        return MessageResponse.ok("LossProduct successfully deleted");
    }
}
