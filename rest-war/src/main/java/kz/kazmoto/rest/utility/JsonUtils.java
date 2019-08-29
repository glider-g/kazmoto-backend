package kz.kazmoto.rest.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.exceptions.ValidateCodeException;
import kz.kazmoto.glob.utils.StdEjb;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonUtils {
    public static ObjectNode createObjectNode() {
        return new ObjectMapper().createObjectNode();
    }
    public static ArrayNode createArrayNode() {
        return new ObjectMapper().createArrayNode();
    }

    public static <T> T getValue(JsonNode node, String key, Class<T> returnType) {
        return getValue(node, key, returnType, false);
    }

    public static <T> T getValue(JsonNode node, String  key, StdEjb<T> stdEjb){
        Long categoryId = getValue(node, key, Long.class, false);
        T entity = stdEjb.findById(categoryId);
        if (entity == null) throw new NotFoundCodeException(key + "is required");
        return entity;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(JsonNode node, String key, Class<T> returnType, boolean nullable){
        String[] keys = key.split("\\.");
        for (String keyPart : keys) {
            node  = node.get(keyPart) != null ? node.get(keyPart) : NullNode.getInstance();
        }

        T result;
        if(node.isNull())
            result = null;
        else if(String.class.equals(returnType))
            result = (T) node.asText();
        else if(Integer.class.equals(returnType))
            result = (T) (Integer) node.asInt();
        else if(Long.class.equals(returnType))
            result = (T) (Long) node.longValue();
        else if(BigDecimal.class.equals(returnType))
            result = (T) node.decimalValue();
        else if(BigInteger.class.equals(returnType))
            result = (T) node.bigIntegerValue();
        else
            throw new IllegalArgumentException(String.format("Can not convert value to %s", returnType));

        if (!nullable && result == null)
            throw new ValidateCodeException(String.format("Field %s required", key));

        return result;
    }
}
