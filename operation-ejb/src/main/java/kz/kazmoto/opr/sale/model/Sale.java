package kz.kazmoto.opr.sale.model;

import kz.kazmoto.glob.exceptions.BadRequestCodeException;
import kz.kazmoto.org.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "opr__sale")
@NamedQueries({
        @NamedQuery(name = "Sale.findById",
                query = "SELECT s FROM Sale s " +
                        "WHERE s.id = :id "),
        @NamedQuery(name = "Sale.findByFilter",
                query = "SELECT s " +
                        "FROM Sale s " +
                        "JOIN FETCH s.manager " +
                        "WHERE (:userId is null OR s.manager.id = :userId) " +
                        "AND (:type is null OR s.type = :type) " +
                        "AND (:customer is null OR s.customer = :customer) " +
                        "AND (:active is null OR s.active = :active) "),
})
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @NotNull
    private String comment;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String customer;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User user) {
        this.manager = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public enum Type {
        RETAIL("retail"),
        WHOLESALE("wholesale");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public static Type findByName(String name){
            switch (name){
                case "retail":
                    return Type.RETAIL;
                case "wholesale":
                    return Type.WHOLESALE;
                default:
                    throw new BadRequestCodeException("Sale type not correct");
            }
        }

        public Type setName(String name) {
            this.name = name;
            return this;
        }
    }

}
