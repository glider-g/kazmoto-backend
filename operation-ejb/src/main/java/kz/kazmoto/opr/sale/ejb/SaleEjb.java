package kz.kazmoto.opr.sale.ejb;


import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.glob.exceptions.NotFoundCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.opr.stockreport.ejb.StockReportEjb;
import kz.kazmoto.opr.sale.model.Sale;
import kz.kazmoto.opr.sale.model.SaleProduct;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class SaleEjb {
    @PersistenceContext(unitName = "kazmoto-opr")
    private EntityManager em;

    private @EJB SaleProductEjb saleProductEjb;
    private @EJB StockReportEjb stockReportEjb;

    public Sale findById(Long id) {
        TypedQuery<Sale> q = em.createNamedQuery("Sale.findById", Sale.class);
        q.setParameter("id", id);
        return EJBUtils.getSingleResult(q);
    }

    public List<Sale> findByFilter(Long userId, Sale.Type type, String customer, Boolean active) {
        TypedQuery<Sale> q = em.createNamedQuery("Sale.findByFilter", Sale.class);
        q.setParameter("userId", userId);
        q.setParameter("type", type);
        q.setParameter("customer", customer);
        q.setParameter("active", active);
        return q.getResultList();
    }

    public Sale create(Sale sale) {
        sale.setActive(Boolean.FALSE);
        return em.merge(sale);
    }

    public void activate(Long saleId){
        Sale sale = findById(saleId);
        if(sale == null) throw new NotFoundCodeException("Sale not found");
        if (sale.isActive())throw new BadRequestCodeException("Sale already activated");
        if (saleProductEjb.countBySale(sale.getId())==0L){
            throw new BadRequestCodeException("Sale cant be empty (without any product)");
        }
        sale.setActive(true);
        em.persist(sale);
        List<SaleProduct> saleProducts = saleProductEjb.findBySale(sale.getId());
        stockReportEjb.createReport(saleProducts);
    }

    public void remove(Sale sale) {
        if (sale.isActive())throw new BadRequestCodeException("Sale already activated");
        if (saleProductEjb.countBySale(sale.getId())!=0L)throw new BadRequestCodeException("Can't remove not empty sale");
        em.remove(sale);
    }
}
