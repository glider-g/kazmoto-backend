package kz.kazmoto.opr.loss.model;

import kz.kazmoto.org.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "opr__loss")
@NamedQueries({
        @NamedQuery(name = "Loss.findById",
                query = "SELECT s FROM Loss s " +
                        "WHERE s.id = :id "),
        @NamedQuery(name = "Loss.findByFilter",
                query = "SELECT s " +
                        "FROM Loss s " +
                        "JOIN FETCH s.manager " +
                        "WHERE (:managerId is null OR s.manager.id = :managerId) " +
                        "AND (:active is null OR s.active = :active) "),
})
public class Loss {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @NotNull
    private String comment;

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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
