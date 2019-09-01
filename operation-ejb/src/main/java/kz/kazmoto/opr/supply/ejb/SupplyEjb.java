package kz.kazmoto.opr.supply.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.stockreport.model.StockReport;
import kz.kazmoto.opr.supply.ejb.SupplyProductEjb;
import kz.kazmoto.opr.supply.model.Supply;
import kz.kazmoto.opr.supply.model.SupplyProduct;
import kz.kazmoto.opr.stockreport.ejb.StockReportEjb;

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
public class SupplyEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    private @EJB SupplyProductEjb supplyProductEjb;
    private @EJB StockReportEjb stockReportEjb;

    public Supply findById(Long id) {
        TypedQuery<Supply> q = em.createNamedQuery("Supply.findById", Supply.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<Supply> findByFilter(Long managerId, String supplier, Boolean active) {
        TypedQuery<Supply> q = em.createNamedQuery("Supply.findByFilter", Supply.class);
        q.setParameter("managerId", managerId);
        q.setParameter("supplier", supplier);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public Supply create(Supply supply) {
        supply.setActive(Boolean.FALSE);
        return em.merge(supply);
    }

    public void activate(Long supplyId){
        Supply supply = findById(supplyId);
        if(supply == null) throw new NotFoundCodeException("Supply not found");
        if (supply.isActive())throw new BadRequestCodeException("Supply already activated");
        if (supplyProductEjb.countBySupply(supply.getId())==0L){
            throw new BadRequestCodeException("Supply cant be empty (without any product)");
        }
        supply.setActive(true);
        em.persist(supply);
        List<SupplyProduct> supplyProducts = supplyProductEjb.findBySupply(supply.getId());


        List<StockReportEjb.StockChange> stockChanges = supplyProducts.stream().map(supplyProduct ->
                new StockReportEjb.StockChange(
                        supplyProduct.getId(),
                        supplyProduct.getProduct(),
                        supplyProduct.getQuantity()))
                .collect(Collectors.toList());
        stockReportEjb.createReport(stockChanges, StockReport.OperationType.SUPPLY);
    }

    public void remove(Supply supply) {
        if (supply.isActive())throw new BadRequestCodeException("Supply already activated");
        if (supplyProductEjb.countBySupply(supply.getId())!=0L)throw new BadRequestCodeException("Can't remove not empty supply");
        em.remove(supply);
    }
}
