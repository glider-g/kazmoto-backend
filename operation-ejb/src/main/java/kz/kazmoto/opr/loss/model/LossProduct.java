package kz.kazmoto.opr.loss.model;

import kz.kazmoto.nom.model.Product;
import kz.kazmoto.opr.loss.model.Loss;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "opr__loss_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"loss_id", "product_id"}),
})
@NamedQueries({
        @NamedQuery(name = "LossProduct.findById",
                query = "SELECT sp FROM LossProduct sp " +
                        "WHERE sp.id = :id "),
        @NamedQuery(name = "LossProduct.findByLossAndProduct",
                query = "SELECT sp FROM LossProduct sp " +
                        "WHERE sp.loss.id = :lossId " +
                        "AND (:productId is null OR sp.product.id = :productId) "),
        @NamedQuery(name = "LossProduct.countByLoss",
                query = "SELECT COUNT(sp) FROM LossProduct sp " +
                        "WHERE sp.loss.id = :lossId "),
})
public class LossProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loss_id")
    private Loss loss;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private BigInteger quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Loss getLoss() {
        return loss;
    }

    public void setLoss(Loss loss) {
        this.loss = loss;
    }

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
