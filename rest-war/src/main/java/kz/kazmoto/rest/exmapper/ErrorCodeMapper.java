package kz.kazmoto.rest.exmapper;

import kz.kazmoto.glob.exceptions.ErrorCodeException;
import kz.kazmoto.rest.utility.MessageResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErrorCodeMapper implements ExceptionMapper<ErrorCodeException> {
    @Override
    public Response toResponse(ErrorCodeException e) {
        return MessageResponse.create(e.getStatus(), e.getMessage(), e.getCode());
    }
}
