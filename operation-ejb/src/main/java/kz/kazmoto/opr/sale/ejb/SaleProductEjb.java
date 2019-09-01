package kz.kazmoto.opr.sale.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.sale.model.SaleProduct;

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

    public SaleProduct create(SaleProduct saleProduct) {
        SaleProduct oldSaleProduct = findBySaleAndProduct(saleProduct.getSale().getId(), saleProduct.getProduct().getId());
        if (oldSaleProduct != null) throw new BadRequestCodeException("product already added to sale");

        return em.merge(saleProduct);
    }

    public void remove(Long id) {
        SaleProduct saleProduct = findById(id);

        if(saleProduct == null) throw new NotFoundCodeException("sale not found");
        if(saleProduct.getSale().isActive()) throw new BadRequestCodeException("sale already activated");

        em.remove(saleProduct);
    }
}
