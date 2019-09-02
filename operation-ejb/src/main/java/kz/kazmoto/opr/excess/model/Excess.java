package kz.kazmoto.opr.excess.model;

import kz.kazmoto.org.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "opr__excess")
@NamedQueries({
        @NamedQuery(name = "Excess.findById",
                query = "SELECT s FROM Excess s " +
                        "WHERE s.id = :id "),
        @NamedQuery(name = "Excess.findByFilter",
                query = "SELECT s " +
                        "FROM Excess s " +
                        "JOIN FETCH s.manager " +
                        "WHERE (:managerId is null OR s.manager.id = :managerId) " +
                        "AND (:active is null OR s.active = :active) "),
})
public class Excess {
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
