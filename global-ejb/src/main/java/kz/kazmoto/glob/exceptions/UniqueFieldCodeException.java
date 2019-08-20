package kz.kazmoto.glob.exceptions;

import javax.ws.rs.core.Response;

public class UniqueFieldCodeException extends ErrorCodeException {
    public UniqueFieldCodeException(String message) {
        super(message, Response.Status.BAD_REQUEST, 2000);
    }
}
