package kz.kazmoto.opr.excess.model;

import kz.kazmoto.nom.model.Product;
import kz.kazmoto.opr.excess.model.Excess;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "opr__excess_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"excess_id", "product_id"}),
})
@NamedQueries({
        @NamedQuery(name = "ExcessProduct.findById",
                query = "SELECT sp FROM ExcessProduct sp " +
                        "WHERE sp.id = :id "),
        @NamedQuery(name = "ExcessProduct.findByExcessAndProduct",
                query = "SELECT sp FROM ExcessProduct sp " +
                        "WHERE sp.excess.id = :excessId " +
                        "AND (:productId is null OR sp.product.id = :productId) "),
        @NamedQuery(name = "ExcessProduct.countByExcess",
                query = "SELECT COUNT(sp) FROM ExcessProduct sp " +
                        "WHERE sp.excess.id = :excessId "),
})
public class ExcessProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excess_id")
    private Excess excess;

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

    public Excess getExcess() {
        return excess;
    }

    public void setExcess(Excess excess) {
        this.excess = excess;
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
