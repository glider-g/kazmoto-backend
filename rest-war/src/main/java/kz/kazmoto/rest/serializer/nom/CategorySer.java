package kz.kazmoto.rest.serializer.nom;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kz.kazmoto.nom.model.Category;
import kz.kazmoto.rest.utility.Serializer;

import static kz.kazmoto.rest.utility.JsonUtils.createObjectNode;


public class CategorySer extends Serializer<Category> {
    @Override
    protected ObjectNode serialize(Category category) {
        ObjectNode node = createObjectNode();
        node.put("id", category.getId());
        node.put("name", category.getName());
        return node;
    }
}
