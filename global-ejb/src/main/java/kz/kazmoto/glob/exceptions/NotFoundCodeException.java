package kz.kazmoto.glob.exceptions;

import javax.ws.rs.core.Response;

public class NotFoundCodeException extends ErrorCodeException {
    public NotFoundCodeException(String message) {
        super(message, Response.Status.NOT_FOUND, 2000);
    }
}
