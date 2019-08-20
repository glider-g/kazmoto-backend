package kz.kazmoto.nom.ejb;


import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.nom.model.Product;
import kz.kazmoto.nom.model.Stock;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.math.BigInteger;

@Stateless
@LocalBean
public class StockEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    public Stock findByProduct(Long productId) {
        TypedQuery<Stock> q = em.createNamedQuery("Stock.findByProduct", Stock.class);
        q.setParameter("productId", productId);
        return EJBUtils.getSingleResult(q);
    }

    @Deprecated
    public BigInteger getQuantityByProduct(Long productId) {
        Query q = em.createNamedQuery("Stock.getQuantityByProduct", Stock.class);
        q.setParameter("productId", productId);
        try {
            return (BigInteger) q.getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalArgumentException("stock not found", e);
        }
    }

    public void createForProduct(Product product){
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setQuantity(BigInteger.ZERO);
        em.persist(stock);
    }

    public void changeQuantity(Long productId, BigInteger quantityChange) {
        Stock stock = findByProduct(productId);
        BigInteger oldQuantity = stock.getQuantity();

        stock.setQuantity(oldQuantity.add(quantityChange));

        em.merge(stock);
    }
}
