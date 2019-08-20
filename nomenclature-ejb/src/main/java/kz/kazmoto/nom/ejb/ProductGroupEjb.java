package kz.kazmoto.nom.ejb;


import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.nom.model.ProductGroup;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ProductGroupEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    public ProductGroup findById(Long id) {
        return em.find(ProductGroup.class, id);
    }

    public List<ProductGroup> findByNameLike(String name) {
        TypedQuery<ProductGroup> q = em.createNamedQuery("ProductGroup.findByNameLike", ProductGroup.class);
        q.setParameter("name", name);
        return q.getResultList();
    }

    private ProductGroup findByName(String name) {
        TypedQuery<ProductGroup> q = em.createNamedQuery("ProductGroup.findByName", ProductGroup.class);
        q.setParameter("name", name);
        return EJBUtils.getSingleResult(q);
    }

    public ProductGroup create(ProductGroup productGroup) {
        ProductGroup anotherProductGroup = findByName(productGroup.getName());
        if (anotherProductGroup != null)
            throw new UniqueFieldCodeException("ProductGroup with this name already exist");

        return em.merge(productGroup);
    }

    public ProductGroup update(ProductGroup editedProductGroup) {
        ProductGroup anotherProductGroup = findByName(editedProductGroup.getName());
        if (anotherProductGroup != null && !anotherProductGroup.equals(editedProductGroup))
            throw new UniqueFieldCodeException("ProductGroup with this name already exist");

        ProductGroup productGroup = findById(editedProductGroup.getId());
        productGroup.setName(editedProductGroup.getName());

        return em.merge(productGroup);
    }
}
