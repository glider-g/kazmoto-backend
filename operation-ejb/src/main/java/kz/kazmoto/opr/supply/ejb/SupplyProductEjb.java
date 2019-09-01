package kz.kazmoto.opr.supply.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.supply.model.SupplyProduct;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class SupplyProductEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    public SupplyProduct findById(Long id) {
        TypedQuery<SupplyProduct> q = em.createNamedQuery("SupplyProduct.findById", SupplyProduct.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<SupplyProduct> findBySupply(Long supplyId) {
        TypedQuery<SupplyProduct> q = em.createNamedQuery("SupplyProduct.findBySupplyAndProduct", SupplyProduct.class);
        q.setParameter("supplyId", supplyId);
        q.setParameter("productId", null);
        return q.getResultList();
    }

    private SupplyProduct findBySupplyAndProduct(Long supplyId, Long productId) {
        TypedQuery<SupplyProduct> q = em.createNamedQuery("SupplyProduct.findBySupplyAndProduct", SupplyProduct.class);
        q.setParameter("supplyId", supplyId);
        q.setParameter("productId", productId);
        return EJBUtils.getSingleResult(q);
    }

    public Long countBySupply(Long supplyId) {
        TypedQuery<Long> q = em.createNamedQuery("SupplyProduct.countBySupply", Long.class);
        q.setParameter("supplyId", supplyId);
        return EJBUtils.getSingleResult(q, 0L);
    }

    public SupplyProduct create(SupplyProduct supplyProduct) {
        SupplyProduct oldSupplyProduct = findBySupplyAndProduct(supplyProduct.getSupply().getId(), supplyProduct.getProduct().getId());
        if (oldSupplyProduct != null) throw new BadRequestCodeException("product already added to supply");

        return em.merge(supplyProduct);
    }

    public void remove(Long id) {
        SupplyProduct supplyProduct = findById(id);

        if(supplyProduct == null) throw new NotFoundCodeException("supply not found");
        if(supplyProduct.getSupply().isActive()) throw new BadRequestCodeException("supply already activated");

        em.remove(supplyProduct);
    }
}
