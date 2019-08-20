package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "nom__product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"model", "device_id", "product_group_id"}),
})
@NamedQueries({
        @NamedQuery(name = "Product.findByProductGroup",
                query = "SELECT p FROM Product p " +
                        "WHERE p.productGroup.id = :productGroupId "),
        @NamedQuery(name = "Product.findByFilter",
                query = "SELECT p FROM Product p " +
                        "WHERE p.model LIKE CONCAT('%',:model,'%') " +
                        "AND p.productGroup.name LIKE CONCAT('%',:name,'%') " +
                        "AND (:deviceId IS NULL OR p.device.id = :deviceId) " +
                        "AND p.barcode LIKE CONCAT('%',:barcode,'%') "),
        @NamedQuery(name = "Product.findByUniques",
                query = "SELECT p FROM Product p " +
                        "WHERE p.model = :model " +
                        "AND p.productGroup.id = :productGroupId " +
                        "AND p.device.id = :deviceId "),
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "barcode", unique = true, updatable = false)
    private String barcode;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "device_id")
    private Device device;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;

    private BigDecimal price;

    private BigDecimal purchasePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
