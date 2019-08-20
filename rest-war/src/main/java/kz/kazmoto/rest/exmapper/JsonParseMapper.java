package kz.kazmoto.rest.exmapper;

import com.fasterxml.jackson.core.JsonParseException;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonParseMapper implements ExceptionMapper<JsonParseException> {
    @Override
    public Response toResponse(JsonParseException e) {
        return MessageResponse.create(Response.Status.BAD_REQUEST, "json format not correct", 2000);
    }
}
