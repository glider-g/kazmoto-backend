package kz.kazmoto.opr.excess.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.excess.model.ExcessProduct;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ExcessProductEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    public ExcessProduct findById(Long id) {
        TypedQuery<ExcessProduct> q = em.createNamedQuery("ExcessProduct.findById", ExcessProduct.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<ExcessProduct> findByExcess(Long excessId) {
        TypedQuery<ExcessProduct> q = em.createNamedQuery("ExcessProduct.findByExcessAndProduct", ExcessProduct.class);
        q.setParameter("excessId", excessId);
        q.setParameter("productId", null);
        return q.getResultList();
    }

    private ExcessProduct findByExcessAndProduct(Long excessId, Long productId) {
        TypedQuery<ExcessProduct> q = em.createNamedQuery("ExcessProduct.findByExcessAndProduct", ExcessProduct.class);
        q.setParameter("excessId", excessId);
        q.setParameter("productId", productId);
        return EJBUtils.getSingleResult(q);
    }

    public Long countByExcess(Long excessId) {
        TypedQuery<Long> q = em.createNamedQuery("ExcessProduct.countByExcess", Long.class);
        q.setParameter("excessId", excessId);
        return EJBUtils.getSingleResult(q, 0L);
    }

    public ExcessProduct create(ExcessProduct excessProduct) {
        ExcessProduct oldExcessProduct = findByExcessAndProduct(excessProduct.getExcess().getId(), excessProduct.getProduct().getId());
        if (oldExcessProduct != null) throw new BadRequestCodeException("product already added to excess");

        return em.merge(excessProduct);
    }

    public void remove(Long id) {
        ExcessProduct excessProduct = findById(id);

        if(excessProduct == null) throw new NotFoundCodeException("excess not found");
        if(excessProduct.getExcess().isActive()) throw new BadRequestCodeException("excess already activated");

        em.remove(excessProduct);
    }
}
