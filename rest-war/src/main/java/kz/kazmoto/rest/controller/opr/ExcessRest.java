package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.opr.excess.ejb.ExcessEjb;
import kz.kazmoto.opr.excess.model.Excess;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.serializer.opr.ExcessSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("opr/excesses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExcessRest {
    @EJB private ExcessEjb excessEjb;

    private ExcessSer excessSer = new ExcessSer();

    @GET
    public Response list(
            @QueryParam("managerId") Long managerId,
            @QueryParam("active") Boolean active) {

        List<Excess> excesses = excessEjb.findByFilter(managerId, active);
        return Response.ok(excessSer.convert(excesses)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Excess excess = excessEjb.findById(id);
        if (excess == null){
            throw new NotFoundCodeException("Excess with this id not exist");
        }
        ObjectNode excessNode = excessSer.convert(excess);
        excessNode.put("excessProducts","");
        return Response.ok(excessNode).build();
    }

    @POST
    public Response create(JsonNode reqBody, @Context SecurityContext securityContext) {
        Excess excess = new Excess();
        excess.setManager((User) securityContext.getUserPrincipal());
        excess.setComment(getValue(reqBody,"comment", String.class));

        Excess savedExcess = excessEjb.create(excess);
        return Response.ok(excessSer.convert(savedExcess)).build();
    }

    @POST
    @Path("activate")
    public Response activate(@QueryParam("id") Long id) {
        if (id==null) throw new BadRequestCodeException("query param \"id\" required");

        excessEjb.activate(id);
        return MessageResponse.ok("Excess activated successfully");
    }
}
