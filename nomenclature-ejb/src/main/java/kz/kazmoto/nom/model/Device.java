package kz.kazmoto.nom.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "nom__device")
@NamedQueries({
        @NamedQuery(name = "Device.findAll",
                query = "SELECT d " +
                        "FROM Device d "),
        @NamedQuery(name = "Device.findByNameExact",
                query = "SELECT d " +
                        "FROM Device d " +
                        "WHERE d.name = :name "),
        @NamedQuery(name = "Device.findByCode",
                query = "SELECT d " +
                        "FROM Device d " +
                        "WHERE d.code = :code ")
})
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @Min(0)
    @Max(99)
    @NotNull
    @Column(unique = true)
    private Integer code;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id.equals(device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
