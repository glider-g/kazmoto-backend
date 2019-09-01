package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.opr.sale.ejb.SaleEjb;
import kz.kazmoto.opr.sale.model.Sale;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.serializer.opr.SaleSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("opr/sales")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SaleRest {
    @EJB private SaleEjb saleEjb;

    private SaleSer saleSer = new SaleSer();

    @GET
    public Response list(
            @QueryParam("userId") Long userId,
            @QueryParam("type") String typeName,
            @QueryParam("customer") String customer,
            @QueryParam("active") Boolean active) {

        Sale.Type type = typeName!=null?Sale.Type.findByName(typeName):null;
        List<Sale> sales = saleEjb.findByFilter(userId, type, customer, active);
        return Response.ok(saleSer.convert(sales)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Sale sale = saleEjb.findById(id);
        if (sale == null){
            throw new NotFoundCodeException("Sale with this id not exist");
        }
        ObjectNode saleNode = saleSer.convert(sale);
        saleNode.put("saleProducts","");
        return Response.ok(saleNode).build();
    }

    @POST
    public Response create(JsonNode reqBody, @Context SecurityContext securityContext) {
        Sale sale = new Sale();
        sale.setManager((User) securityContext.getUserPrincipal());
        sale.setComment(getValue(reqBody,"comment", String.class));
        sale.setType(getValue(reqBody, "type", String.class, Sale.Type::findByName));
        sale.setCustomer(getValue(reqBody,"customer", String.class));

        Sale savedSale = saleEjb.create(sale);
        return Response.ok(saleSer.convert(savedSale)).build();
    }

    @POST
    @Path("activate")
    public Response activate(@QueryParam("id") Long id) {
        if (id==null) throw new BadRequestCodeException("query param \"id\" required");

        saleEjb.activate(id);
        return MessageResponse.ok("Sale activated successfully");
    }
}
