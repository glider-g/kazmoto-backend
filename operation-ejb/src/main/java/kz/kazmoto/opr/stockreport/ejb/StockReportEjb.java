package kz.kazmoto.opr.stockreport.ejb;


import kz.kazmoto.nom.ejb.StockEjb;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.opr.sale.model.SaleProduct;
import kz.kazmoto.opr.stockreport.model.StockReport;
import kz.kazmoto.opr.supply.model.SupplyProduct;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@LocalBean
public class StockReportEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    @EJB
    StockEjb stockEjb;

    public void createReport(List<StockChange> stockChanges, StockReport.OperationType operationType) {
        stockChanges.sort(Comparator.comparingLong(stockChange -> stockChange.getProduct().getId()));

        for (StockChange stockChange : stockChanges) {
            BigInteger oldQuantity = stockEjb.getQuantityByProduct(stockChange.getProduct().getId());
            BigInteger change = operationType.isAddition() ? stockChange.getQuantity() : stockChange.getQuantity().negate();
            stockEjb.changeQuantity(stockChange.getProduct().getId(), change);
            BigInteger newQuantity = stockEjb.getQuantityByProduct(stockChange.getProduct().getId());

            StockReport stockReport = new StockReport();
            stockReport.setProduct(stockChange.getProduct());
            stockReport.setOperationType(operationType);
            stockReport.setEntityId(stockChange.getEntityId());
            stockReport.setOldQuantity(oldQuantity);
            stockReport.setNewQuantity(newQuantity);
            em.persist(stockReport);
        }
    }

    public List<StockReport> findByProduct(Long productId) {
        TypedQuery<StockReport> q = em.createNamedQuery("StockReport.findByProduct", StockReport.class);
        q.setParameter("productId", productId);
        return q.getResultList();
    }

    public static class StockChange {
        private Long entityId;
        private Product product;
        private BigInteger quantity;

        public StockChange(Long entityId, Product product, BigInteger quantity) {
            this.entityId = entityId;
            this.product = product;
            this.quantity = quantity;
        }

        Long getEntityId() {
            return entityId;
        }

        Product getProduct() {
            return product;
        }

        BigInteger getQuantity() {
            return quantity;
        }
    }
}
