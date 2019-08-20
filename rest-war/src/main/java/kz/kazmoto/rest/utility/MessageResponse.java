package kz.kazmoto.rest.utility;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.core.Response;

public class MessageResponse {

    public static Response ok(String msg){
        return create(Response.Status.OK, msg);
    }

    public static Response create(Response.Status status, String msg) {
        return create(status, msg, 1000);
    }

    public static Response create(Response.Status status, String msg, int code) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode resNode = mapper.createObjectNode();
        resNode.put("msg",msg);
        resNode.put("code",code);

        return Response.status(status)
                .entity(resNode)
                .build();
    }
}
