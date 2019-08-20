package kz.kazmoto.rest.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import kz.kazmoto.glob.exceptions.ValidateCodeException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonUtils {

    public static <T> T getValue(JsonNode node, String key, Class<T> returnType) {
        return getValue(node, key, returnType, false);
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
