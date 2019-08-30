package kz.kazmoto.rest.exmapper;

import com.fasterxml.jackson.core.JsonParseException;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserRoleMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        return MessageResponse.create(Response.Status.BAD_REQUEST, "Forbidden", 2000);
    }
}
