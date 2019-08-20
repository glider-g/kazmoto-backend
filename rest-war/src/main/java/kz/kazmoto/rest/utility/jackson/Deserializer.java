package kz.kazmoto.rest.utility.jackson;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class Deserializer<T> {
    public T convert(JsonNode node){
        if(!node.isObject()) throw new IllegalArgumentException("JsonNode is not object");
        T t = deserialize(node);
        validate(t);
        return t;
    }

    public List<T> deserializeArray(JsonNode jsonNode, boolean array){
        if(!jsonNode.isArray()) throw new IllegalArgumentException("JsonNode is not array");
        return StreamSupport.stream(jsonNode.spliterator(), false)
                .map(this::convert)
                .collect(Collectors.toList());
    }

    protected abstract T deserialize(JsonNode node);

    protected void validate(T t){
    }
}
