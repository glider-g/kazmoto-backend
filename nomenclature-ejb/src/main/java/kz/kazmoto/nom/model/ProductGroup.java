package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nom__product_group")
@NamedQueries({
        @NamedQuery(name = "ProductGroup.findByNameLike",
                query = "SELECT pg FROM ProductGroup pg " +
                        "WHERE pg.name LIKE CONCAT('%',:name,'%') "),
        @NamedQuery(name = "ProductGroup.findByName",
                query = "SELECT pg FROM ProductGroup pg " +
                        "WHERE pg.name = :name "),
})
public class ProductGroup{

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
