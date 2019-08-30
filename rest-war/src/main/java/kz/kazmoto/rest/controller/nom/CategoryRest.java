package kz.kazmoto.rest.controller.nom;

import com.fasterxml.jackson.databind.JsonNode;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.nom.ejb.CategoryEjb;
import kz.kazmoto.nom.model.Category;
import kz.kazmoto.rest.serializer.nom.CategorySer;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static kz.kazmoto.rest.utility.JsonUtils.getValue;


@Path("nom/categories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoryRest {
    @EJB
    private CategoryEjb categoryEjb;

    private CategorySer categorySer = new CategorySer();

    @GET
    public Response list(
            @QueryParam("name") @DefaultValue("") String name) {
        List<Category> categories = categoryEjb.findByName(name);
        return Response.ok(categorySer.convert(categories)).build();
    }

    @GET
    @Path("{id}")
    public Response retrieve(@PathParam("id") Long id) {
        Category category = categoryEjb.findById(id);
        if (category == null){
            throw new NotFoundCodeException("Category with this id not exist");
        }
        return Response.ok(categorySer.convert(category)).build();
    }

    @POST
    public Response create(JsonNode reqBody) {
        Category category = new Category();
        category.setName(getValue(reqBody,"name", String.class));

        Category savedCategory = categoryEjb.create(category);
        return Response.ok(categorySer.convert(savedCategory)).build();
    }

    @POST
    @Path("{id}")
    public Response update(@PathParam("id") Long id, JsonNode reqBody) {
        Category category = categoryEjb.findById(id);
        if (category == null){
            throw new NotFoundCodeException("Category with this id not exist");
        }
        category.setName(getValue(reqBody,"name", String.class));

        Category savedCategory = categoryEjb.update(category);
        return Response.ok(categorySer.convert(savedCategory)).build();
    }
}
