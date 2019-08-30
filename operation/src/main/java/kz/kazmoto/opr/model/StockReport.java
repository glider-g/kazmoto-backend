package kz.kazmoto.opr.model;

import kz.kazmoto.nom.model.Product;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Table(name = "opr__stock_report")
@NamedQueries({
        @NamedQuery(name = "StockReport.findByProduct",
                query = "SELECT sr FROM StockReport sr " +
                        "WHERE sr.product.id = :productId "),
})
public class StockReport {
    @Id
    private Long id;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    private OperationType operationType;

    @Column(name = "entity_id")
    private Long entityId;

    @NotNull
    private BigInteger oldQuantity;

    @NotNull
    private BigInteger newQuantity;

    public StockReport() {
    }

    public Long getId() {
        return id;
    }

    public StockReport setId(Long id) {
        this.id = id;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public StockReport setProduct(Product product) {
        this.product = product;
        return this;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public StockReport setOperationType(OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public Long getEntityId() {
        return entityId;
    }

    public StockReport setEntityId(Long itemId) {
        this.entityId = itemId;
        return this;
    }

    public BigInteger getOldQuantity() {
        return oldQuantity;
    }

    public StockReport setOldQuantity(BigInteger oldQuantity) {
        this.oldQuantity = oldQuantity;
        return this;
    }

    public BigInteger getNewQuantity() {
        return newQuantity;
    }

    public StockReport setNewQuantity(BigInteger newQuantity) {
        this.newQuantity = newQuantity;
        return this;
    }

    public enum OperationType {
        SALE(false),
        SUPPLY(true),
        DECOMMISSION(false),
        DEBIT(true);
        private boolean addition;
        OperationType(boolean addition){
            this.addition = addition;
        }

        public boolean isAddition() {
            return addition;
        }
    }

}
