package kz.kazmoto.opr.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.model.SaleProduct;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class SaleProductEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    public SaleProduct findById(Long id) {
        TypedQuery<SaleProduct> q = em.createNamedQuery("SaleProduct.findById", SaleProduct.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<SaleProduct> findBySale(Long saleId) {
        TypedQuery<SaleProduct> q = em.createNamedQuery("SaleProduct.findBySaleAndProduct", SaleProduct.class);
        q.setParameter("saleId", saleId);
        q.setParameter("productId", null);
        return q.getResultList();
    }

    private SaleProduct findBySaleAndProduct(Long saleId, Long productId) {
        TypedQuery<SaleProduct> q = em.createNamedQuery("SaleProduct.findBySaleAndProduct", SaleProduct.class);
        q.setParameter("saleId", saleId);
        q.setParameter("productId", productId);
        return EJBUtils.getSingleResult(q);
    }

    public Long countBySale(Long saleId) {
        TypedQuery<Long> q = em.createNamedQuery("SaleProduct.countBySale", Long.class);
        q.setParameter("saleId", saleId);
        return EJBUtils.getSingleResult(q, 0L);
    }

    public void create(SaleProduct saleProduct) {
        SaleProduct oldSaleProduct = findBySaleAndProduct(saleProduct.getSale().getId(), saleProduct.getProduct().getId());
        if (oldSaleProduct != null) throw new BadRequestCodeException("product already added to sale");

        em.persist(saleProduct);
    }

    public void remove(SaleProduct saleProduct) {
        if(saleProduct.getSale().isActive()) throw new BadRequestCodeException("sale already activated");
        em.remove(em.merge(saleProduct));
    }
}
