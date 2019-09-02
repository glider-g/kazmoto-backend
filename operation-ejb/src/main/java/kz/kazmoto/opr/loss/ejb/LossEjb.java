package kz.kazmoto.opr.loss.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.stockreport.ejb.StockReportEjb;
import kz.kazmoto.opr.stockreport.model.StockReport;
import kz.kazmoto.opr.loss.ejb.LossProductEjb;
import kz.kazmoto.opr.loss.model.Loss;
import kz.kazmoto.opr.loss.model.LossProduct;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class LossEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    private @EJB LossProductEjb lossProductEjb;
    private @EJB StockReportEjb stockReportEjb;

    public Loss findById(Long id) {
        TypedQuery<Loss> q = em.createNamedQuery("Loss.findById", Loss.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<Loss> findByFilter(Long managerId, Boolean active) {
        TypedQuery<Loss> q = em.createNamedQuery("Loss.findByFilter", Loss.class);
        q.setParameter("managerId", managerId);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public Loss create(Loss loss) {
        loss.setActive(Boolean.FALSE);
        return em.merge(loss);
    }

    public void activate(Long lossId){
        Loss loss = findById(lossId);
        if(loss == null) throw new NotFoundCodeException("Loss not found");
        if (loss.isActive())throw new BadRequestCodeException("Loss already activated");
        if (lossProductEjb.countByLoss(loss.getId())==0L){
            throw new BadRequestCodeException("Loss cant be empty (without any product)");
        }
        loss.setActive(true);
        em.persist(loss);
        List<LossProduct> lossProducts = lossProductEjb.findByLoss(loss.getId());


        List<StockReportEjb.StockChange> stockChanges = lossProducts.stream().map(lossProduct ->
                new StockReportEjb.StockChange(
                        lossProduct.getId(),
                        lossProduct.getProduct(),
                        lossProduct.getQuantity()))
                .collect(Collectors.toList());
        stockReportEjb.createReport(stockChanges, StockReport.OperationType.LOSS);
    }

    public void remove(Loss loss) {
        if (loss.isActive())throw new BadRequestCodeException("Loss already activated");
        if (lossProductEjb.countByLoss(loss.getId())!=0L)throw new BadRequestCodeException("Can't remove not empty loss");
        em.remove(loss);
    }
}
