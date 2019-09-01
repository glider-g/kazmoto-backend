package kz.kazmoto.opr.supply.model;

import kz.kazmoto.nom.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "opr__supply_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"supply_id", "product_id"}),
})
@NamedQueries({
        @NamedQuery(name = "SupplyProduct.findById",
                query = "SELECT sp FROM SupplyProduct sp " +
                        "INNER JOIN FETCH sp.supply s " +
                        "WHERE sp.id = :id "),
        @NamedQuery(name = "SupplyProduct.findBySupplyAndProduct",
                query = "SELECT sp FROM SupplyProduct sp " +
                        "WHERE sp.supply.id = :supplyId " +
                        "AND (:productId is null OR sp.product.id = :productId) "),
        @NamedQuery(name = "SupplyProduct.countBySupply",
                query = "SELECT COUNT(sp) FROM SupplyProduct sp " +
                        "WHERE sp.supply.id = :supplyId "),
})
public class SupplyProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private BigDecimal purchasePrice;

    @NotNull
    private BigInteger quantity;

    public SupplyProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supply getSupply() {
        return supply;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal price) {
        this.purchasePrice = price;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

}
