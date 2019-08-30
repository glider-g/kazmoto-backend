package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.nom.ejb.ProductEjb;
import kz.kazmoto.opr.ejb.SaleEjb;
import kz.kazmoto.opr.ejb.SaleProductEjb;
import kz.kazmoto.opr.model.SaleProduct;
import kz.kazmoto.rest.serializer.opr.SaleProductSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("opr/sale-products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SaleProductRest {

    @EJB private SaleProductEjb saleProductEjb;
    @EJB private SaleEjb saleEjb;
    @EJB private ProductEjb productEjb;

    private SaleProductSer saleProductSer = new SaleProductSer();

    @GET
    public Response list(
            @QueryParam("saleId") Long saleId) {
        if (saleId == null) throw new BadRequestCodeException("quarry param 'saleId' required");

        List<SaleProduct> saleProducts = saleProductEjb.findBySale(saleId);
        return Response.ok(saleProductSer.convert(saleProducts)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setSale(getValue(reqBody, "sale.id", Long.class, id -> saleEjb.findById(id)));
        saleProduct.setProduct(getValue(reqBody, "product.id", Long.class, id -> productEjb.findById(id)));
        saleProduct.setPrice(getValue(reqBody,"price", BigDecimal.class));
        saleProduct.setQuantity(getValue(reqBody,"quantity", BigInteger.class));

        SaleProduct savedSaleProduct = saleProductEjb.create(saleProduct);
        return Response.ok(saleProductSer.convert(savedSaleProduct)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        saleProductEjb.remove(id);
        return MessageResponse.ok("SaleProduct successfully deleted");
    }
}
