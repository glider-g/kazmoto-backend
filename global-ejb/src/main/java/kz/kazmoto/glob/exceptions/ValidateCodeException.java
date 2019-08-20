package kz.kazmoto.glob.exceptions;

import javax.ws.rs.core.Response;

public class ValidateCodeException extends ErrorCodeException {
    public ValidateCodeException(String message) {
        super(message, Response.Status.BAD_REQUEST, 2000);
    }
}
