package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;


@Entity
@Table(name = "nom__stock")
@NamedQueries({
        @NamedQuery(name = "Stock.findByProduct",
                query = "SELECT s FROM Stock s " +
                        "WHERE s.product.id = :productId "),
        @NamedQuery(name = "Stock.getQuantityByProduct",
                query = "SELECT s.quantity FROM Stock s " +
                        "WHERE s.product.id = :productId "),
})
public class Stock implements Serializable {
    @Id
    @OneToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private BigInteger quantity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }
}
