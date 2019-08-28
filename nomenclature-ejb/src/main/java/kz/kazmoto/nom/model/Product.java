package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "nom__product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "device_id", "product_group_id"}),
})
@NamedQueries({
        @NamedQuery(name = "Product.findByProductGroup",
                query = "SELECT p FROM Product p " +
                        "WHERE p.category.id = :productGroupId "),
        @NamedQuery(name = "Product.findByFilter",
                query = "SELECT p FROM Product p " +
                        "WHERE p.name LIKE CONCAT('%',:name,'%') " +
                        "AND p.category.name LIKE CONCAT('%',:categoryName,'%') " +
                        "AND p.barcode LIKE CONCAT('%',:barcode,'%') " +
                        "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
                        "AND (:deviceId IS NULL OR p.device.id = :deviceId) " +
                        ""),
        @NamedQuery(name = "Product.findByModelAndCategoryAndDevice",
                query = "SELECT p FROM Product p " +
                        "WHERE p.name = :name " +
                        "AND p.category.id = :categoryId " +
                        "AND p.device.id = :deviceId "),
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "barcode", unique = true, updatable = false)
    private String barcode;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_group_id")
    private Category category;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "device_id")
    private Device device;

    private BigDecimal price;

    private BigDecimal purchasePrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String model) {
        this.name = model;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

