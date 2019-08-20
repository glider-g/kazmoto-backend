package kz.kazmoto.glob.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ErrorCodeException extends WebApplicationException {
    private int code;
    private Response.Status status;

    protected ErrorCodeException(String message, Response.Status status, int code) {
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

    public void setStatus(Response.Status status) {
        this.status = status;
    }
}
