package kz.kazmoto.nom.ejb;


import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.glob.utils.UniqueFieldChecker;
import kz.kazmoto.nom.model.Category;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class CategoryEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    private UniqueFieldChecker<Category> fieldChecker = new UniqueFieldChecker<Category>()
            .addChecker("name", category -> findByNameExact(category.getName()));

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findByName(String name) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findByName", Category.class);
        q.setParameter("name", name);
        return q.getResultList();
    }

    private Category findByNameExact(String name) {
        TypedQuery<Category> q = em.createNamedQuery("Category.findByNameExact", Category.class);
        q.setParameter("name", name);
        return EJBUtils.getSingleResult(q);
    }

    public Category create(Category category) {
        fieldChecker.check(category, false);

        return em.merge(category);
    }

    public Category update(Category category) {
        fieldChecker.check(category, true);

        return em.merge(category);
    }
}
