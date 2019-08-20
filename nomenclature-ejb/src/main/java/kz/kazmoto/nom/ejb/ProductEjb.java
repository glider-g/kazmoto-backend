package kz.kazmoto.nom.ejb;


import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
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

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findByFilter(String model, String name, String barcode, Long deviceId) {
        TypedQuery<Product> q = em.createNamedQuery("Product.findByFilter", Product.class);
        q.setParameter("model", model);
        q.setParameter("name", name);
        q.setParameter("barcode", barcode);
        q.setParameter("deviceId", deviceId);
        return q.getResultList();
    }

    private Product findByUniques(String model, Long productGroupId, Long deviceId) {
        TypedQuery<Product> q = em.createNamedQuery("Product.findByUniques", Product.class);
        q.setParameter("model", model);
        q.setParameter("productGroupId", productGroupId);
        q.setParameter("deviceId", deviceId);
        return EJBUtils.getSingleResult(q);
    }

    public Product create(Product product) {
        Product otherProduct = findByUniques(product.getModel(), product.getProductGroup().getId(), product.getDevice().getId());
        if (otherProduct != null)
            throw new UniqueFieldCodeException("Product with this model already exist");

        product.setBarcode(String.valueOf(System.currentTimeMillis()));

        Product savedProduct = em.merge(product);
        stockEjb.createForProduct(product);
        return savedProduct;
    }

    public Product update(Product editedProduct) {
        Product otherProduct = findByUniques(editedProduct.getModel(), editedProduct.getProductGroup().getId(), editedProduct.getDevice().getId());
        if (otherProduct != null && !otherProduct.equals(editedProduct))
            throw new UniqueFieldCodeException("Product with this model already exist");

        Product product = findById(editedProduct.getId());
        product.setModel(editedProduct.getModel());
        product.setModel(editedProduct.getModel());
        product.setProductGroup(editedProduct.getProductGroup());
        product.setPrice(editedProduct.getPrice());
        product.setPurchasePrice(editedProduct.getPurchasePrice());
        return em.merge(product);
    }
}
