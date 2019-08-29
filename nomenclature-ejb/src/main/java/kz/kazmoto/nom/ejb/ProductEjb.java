package kz.kazmoto.nom.ejb;


import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.glob.utils.UniqueFieldChecker;
import kz.kazmoto.nom.model.Product;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ProductEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    @EJB private StockEjb stockEjb;

    private UniqueFieldChecker<Product> fieldChecker = new UniqueFieldChecker<Product>()
            .addChecker("name", product -> findByModelAndCategoryAndDevice(product.getName(), product.getCategory().getId(), product.getDevice().getId()));

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findByFilter(String name, String categoryName, String barcode, Long categoryId, Long deviceId) {
        TypedQuery<Product> q = em.createNamedQuery("Product.findByFilter", Product.class);
        q.setParameter("name", name);
        q.setParameter("categoryName", categoryName);
        q.setParameter("barcode", barcode);
        q.setParameter("categoryId", categoryId);
        q.setParameter("deviceId", deviceId);
        return q.getResultList();
    }

    private Product findByModelAndCategoryAndDevice(String name, Long categoryId, Long deviceId) {
        TypedQuery<Product> q = em.createNamedQuery("Product.findByModelAndCategoryAndDevice", Product.class);
        q.setParameter("name", name);
        q.setParameter("categoryId", categoryId);
        q.setParameter("deviceId", deviceId);
        return EJBUtils.getSingleResult(q);
    }

    public Product create(Product product) {
        fieldChecker.check(product, false);

        product.setBarcode(String.valueOf(System.currentTimeMillis()));

        Product savedProduct = em.merge(product);
        stockEjb.createForProduct(product);
        return savedProduct;
    }

    public Product update(Product product) {
        fieldChecker.check(product, true);
        return em.merge(product);
    }
}
