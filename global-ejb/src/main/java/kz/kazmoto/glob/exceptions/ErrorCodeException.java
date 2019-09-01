package kz.kazmoto.glob.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationException(rollback = true)
public class ErrorCodeException extends RuntimeException {
    private int code;
    private Response.Status status;

    ErrorCodeException(String message, Response.Status status, int code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Response.Status getStatus() {
        return status;
    }
}
