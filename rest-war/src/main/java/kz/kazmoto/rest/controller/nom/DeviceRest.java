package kz.kazmoto.rest.controller.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.exceptions.ValidateCodeException;
import kz.kazmoto.nom.ejb.DeviceEjb;
import kz.kazmoto.nom.model.Device;
import kz.kazmoto.rest.serializer.nom.DeviceSer;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;

@Path("nom/devices")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeviceRest {
    @EJB
    private DeviceEjb deviceEjb;

    private DeviceSer deviceSer = new DeviceSer();
    @GET
    public Response list() {
        List<Device> devices = deviceEjb.findAll();
        return Response.ok(deviceSer.convert(devices)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Device device = deviceEjb.findById(id);
        if (device == null){
            throw new NotFoundCodeException("Device with this id not exist");
        }
        return Response.ok(deviceSer.convert(device)).build();
    }

    @POST
    public Response create(JsonNode reqNode) {
        Device device = new Device();
        device.setName(getValue(reqNode,"name", String.class));
        device.setCode(getValue(reqNode, "code", Integer.class));

        if (device.getCode()<1 || 99< device.getCode() )
            throw new ValidateCodeException("Code range in 1..99");

        Device savedDevice = deviceEjb.create(device);
        return Response.ok(deviceSer.convert(savedDevice)).build();
    }

    @POST
    @Path("{id}")
    public Response update(@PathParam("id") Long id, JsonNode reqNode) {
        Device device = deviceEjb.findById(id);
        if (device == null){
            throw new NotFoundCodeException("Device with this id not exist");
        }

        device.setName(getValue(reqNode,"name", String.class));

        Device savedDevice = deviceEjb.update(device);
        return Response.ok(deviceSer.convert(savedDevice)).build();
    }
}
