package kz.kazmoto.opr.loss.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.loss.model.LossProduct;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class LossProductEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    public LossProduct findById(Long id) {
        TypedQuery<LossProduct> q = em.createNamedQuery("LossProduct.findById", LossProduct.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<LossProduct> findByLoss(Long lossId) {
        TypedQuery<LossProduct> q = em.createNamedQuery("LossProduct.findByLossAndProduct", LossProduct.class);
        q.setParameter("lossId", lossId);
        q.setParameter("productId", null);
        return q.getResultList();
    }

    private LossProduct findByLossAndProduct(Long lossId, Long productId) {
        TypedQuery<LossProduct> q = em.createNamedQuery("LossProduct.findByLossAndProduct", LossProduct.class);
        q.setParameter("lossId", lossId);
        q.setParameter("productId", productId);
        return EJBUtils.getSingleResult(q);
    }

    public Long countByLoss(Long lossId) {
        TypedQuery<Long> q = em.createNamedQuery("LossProduct.countByLoss", Long.class);
        q.setParameter("lossId", lossId);
        return EJBUtils.getSingleResult(q, 0L);
    }

    public LossProduct create(LossProduct lossProduct) {
        LossProduct oldLossProduct = findByLossAndProduct(lossProduct.getLoss().getId(), lossProduct.getProduct().getId());
        if (oldLossProduct != null) throw new BadRequestCodeException("product already added to loss");

        return em.merge(lossProduct);
    }

    public void remove(Long id) {
        LossProduct lossProduct = findById(id);

        if(lossProduct == null) throw new NotFoundCodeException("loss not found");
        if(lossProduct.getLoss().isActive()) throw new BadRequestCodeException("loss already activated");

        em.remove(lossProduct);
    }
}
