package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.opr.supply.ejb.SupplyEjb;
import kz.kazmoto.opr.supply.model.Supply;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.serializer.opr.SupplySer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("opr/supplies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SupplyRest {
    @EJB private SupplyEjb supplyEjb;

    private SupplySer supplySer = new SupplySer();

    @GET
    public Response list(
            @QueryParam("managerId") Long managerId,
            @QueryParam("supplier") String supplier,
            @QueryParam("active") Boolean active) {

        List<Supply> supplies = supplyEjb.findByFilter(managerId, supplier, active);
        return Response.ok(supplySer.convert(supplies)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Supply supply = supplyEjb.findById(id);
        if (supply == null){
            throw new NotFoundCodeException("Supply with this id not exist");
        }
        ObjectNode supplyNode = supplySer.convert(supply);
        supplyNode.put("supplyProducts","");
        return Response.ok(supplyNode).build();
    }

    @POST
    public Response create(JsonNode reqBody, @Context SecurityContext securityContext) {
        Supply supply = new Supply();
        supply.setManager((User) securityContext.getUserPrincipal());
        supply.setComment(getValue(reqBody,"comment", String.class));
        supply.setSupplier(getValue(reqBody,"supplier", String.class));

        Supply savedSupply = supplyEjb.create(supply);
        return Response.ok(supplySer.convert(savedSupply)).build();
    }

    @POST
    @Path("activate")
    public Response activate(@QueryParam("id") Long id) {
        if (id==null) throw new BadRequestCodeException("query param \"id\" required");

        supplyEjb.activate(id);
        return MessageResponse.ok("Supply activated successfully");
    }
}
