package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.nom.model.Category;
import kz.kazmoto.rest.utility.jackson.Deserializer;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


public class CategoryDer extends Deserializer<Category> {
    @Override
    protected Category deserialize(JsonNode node) {
        Category pGroup = new Category();
        pGroup.setName(getValue(node,"name", String.class));
        return pGroup;
    }
}
