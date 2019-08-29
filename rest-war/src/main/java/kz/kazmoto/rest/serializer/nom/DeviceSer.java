package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.nom.model.Device;
import kz.kazmoto.rest.utility.Serializer;

import javax.ejb.Stateless;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;

@Stateless
public class DeviceSer extends Serializer<Device> {
    @Override
    protected ObjectNode serialize(Device device) {
        ObjectNode node = createObjectNode();
        node.put("id", device.getId());
        node.put("name", device.getName());
        node.put("code", device.getCode());
        return node;
    }
}
