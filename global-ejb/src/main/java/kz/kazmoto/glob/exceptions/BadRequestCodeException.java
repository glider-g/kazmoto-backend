package kz.kazmoto.glob.exceptions;

import javax.ws.rs.core.Response;

public class BadRequestCodeException extends ErrorCodeException {
    public BadRequestCodeException(String message) {
        super(message, Response.Status.BAD_REQUEST, 2000);
    }
}
