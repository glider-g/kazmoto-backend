package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.ValidateCodeException;
import kz.kazmoto.nom.model.Device;
import kz.kazmoto.rest.utility.jackson.Deserializer;

import javax.ejb.Stateless;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Stateless
public class DeviceDer extends Deserializer<Device> {
    @Override
    protected Device deserialize(JsonNode node) {
        Device device = new Device();
        device.setName(getValue(node,"name", String.class));
        device.setCode(getValue(node, "code", Integer.class));
        if (device.getCode()<1 || 99< device.getCode() )
            throw new ValidateCodeException("Code range in 1..99");
        return device;
    }
}
