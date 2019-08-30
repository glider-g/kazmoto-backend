package kz.kazmoto.glob.exceptions;

import javax.ws.rs.core.Response;

public class UnAuthCodeException extends ErrorCodeException {
    public UnAuthCodeException(String message) {
        super(message, Response.Status.UNAUTHORIZED, 2000);
    }
}
