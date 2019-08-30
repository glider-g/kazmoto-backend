package kz.kazmoto.opr.model;

import kz.kazmoto.nom.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "opr__sale_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sale_id", "product_id"}),
})
@NamedQueries({
        @NamedQuery(name = "SaleProduct.findById",
                query = "SELECT sp FROM SaleProduct sp " +
                        "WHERE sp.id = :id "),
        @NamedQuery(name = "SaleProduct.findBySaleAndProduct",
                query = "SELECT sp FROM SaleProduct sp " +
                        "WHERE sp.sale.id = :saleId " +
                        "AND (:productId is null OR p.id = :productId) "),
        @NamedQuery(name = "SaleProduct.countBySale",
                query = "SELECT COUNT(sp) FROM SaleProduct sp " +
                        "WHERE sp.sale.id = :saleId "),
})
public class SaleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigInteger quantity;

    public SaleProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

}
