package kz.kazmoto.rest.controller.opr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.opr.loss.ejb.LossEjb;
import kz.kazmoto.opr.loss.model.Loss;
import kz.kazmoto.org.model.User;
import kz.kazmoto.rest.serializer.opr.LossSer;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("opr/losses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LossRest {
    @EJB private LossEjb lossEjb;

    private LossSer lossSer = new LossSer();

    @GET
    public Response list(
            @QueryParam("managerId") Long managerId,
            @QueryParam("active") Boolean active) {

        List<Loss> losses = lossEjb.findByFilter(managerId, active);
        return Response.ok(lossSer.convert(losses)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Loss loss = lossEjb.findById(id);
        if (loss == null){
            throw new NotFoundCodeException("Loss with this id not exist");
        }
        ObjectNode lossNode = lossSer.convert(loss);
        lossNode.put("lossProducts","");
        return Response.ok(lossNode).build();
    }

    @POST
    public Response create(JsonNode reqBody, @Context SecurityContext securityContext) {
        Loss loss = new Loss();
        loss.setManager((User) securityContext.getUserPrincipal());
        loss.setComment(getValue(reqBody,"comment", String.class));

        Loss savedLoss = lossEjb.create(loss);
        return Response.ok(lossSer.convert(savedLoss)).build();
    }

    @POST
    @Path("activate")
    public Response activate(@QueryParam("id") Long id) {
        if (id==null) throw new BadRequestCodeException("query param \"id\" required");

        lossEjb.activate(id);
        return MessageResponse.ok("Loss activated successfully");
    }
}
