package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nom__category")
@NamedQueries({
        @NamedQuery(name = "Category.findByName",
                query = "SELECT pg FROM Category pg " +
                        "WHERE pg.name LIKE CONCAT('%',:name,'%') "),
        @NamedQuery(name = "Category.findByNameExact",
                query = "SELECT pg FROM Category pg " +
                        "WHERE pg.name = :name "),
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
