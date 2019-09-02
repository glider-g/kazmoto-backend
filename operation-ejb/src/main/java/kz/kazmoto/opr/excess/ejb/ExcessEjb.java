package kz.kazmoto.opr.excess.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.excess.model.Excess;
import kz.kazmoto.opr.excess.model.ExcessProduct;
import kz.kazmoto.opr.stockreport.ejb.StockReportEjb;
import kz.kazmoto.opr.stockreport.model.StockReport;

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
public class ExcessEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    private @EJB ExcessProductEjb excessProductEjb;
    private @EJB StockReportEjb stockReportEjb;

    public Excess findById(Long id) {
        TypedQuery<Excess> q = em.createNamedQuery("Excess.findById", Excess.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<Excess> findByFilter(Long managerId, Boolean active) {
        TypedQuery<Excess> q = em.createNamedQuery("Excess.findByFilter", Excess.class);
        q.setParameter("managerId", managerId);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public Excess create(Excess excess) {
        excess.setActive(Boolean.FALSE);
        return em.merge(excess);
    }

    public void activate(Long excessId){
        Excess excess = findById(excessId);
        if(excess == null) throw new NotFoundCodeException("Excess not found");
        if (excess.isActive())throw new BadRequestCodeException("Excess already activated");
        if (excessProductEjb.countByExcess(excess.getId())==0L){
            throw new BadRequestCodeException("Excess cant be empty (without any product)");
        }
        excess.setActive(true);
        em.persist(excess);
        List<ExcessProduct> excessProducts = excessProductEjb.findByExcess(excess.getId());


        List<StockReportEjb.StockChange> stockChanges = excessProducts.stream().map(excessProduct ->
                new StockReportEjb.StockChange(
                        excessProduct.getId(),
                        excessProduct.getProduct(),
                        excessProduct.getQuantity()))
                .collect(Collectors.toList());
        stockReportEjb.createReport(stockChanges, StockReport.OperationType.EXCESS);
    }

    public void remove(Excess excess) {
        if (excess.isActive())throw new BadRequestCodeException("Excess already activated");
        if (excessProductEjb.countByExcess(excess.getId())!=0L)throw new BadRequestCodeException("Can't remove not empty excess");
        em.remove(excess);
    }
}
