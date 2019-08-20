package kz.kazmoto.rest.utility.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public abstract class Serializer<T>{
    public static ObjectNode createObjectNode() {
        return new ObjectMapper().createObjectNode();
    }
    public static ArrayNode createArrayNode() {
        return new ObjectMapper().createArrayNode();
    }

    public ObjectNode convert(T t){
        return serialize(t);
    }

    public ArrayNode convert(List<T> tList){
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        tList.forEach(t -> arrayNode.add(convert(t)));
        return arrayNode;
    }

    protected abstract ObjectNode serialize(T t);
}
