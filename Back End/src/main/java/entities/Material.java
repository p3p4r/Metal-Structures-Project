package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllMaterials",
                query = "SELECT m FROM Material m ORDER BY m.name" // JPQL
        )
})
public class Material {

    @Id @GeneratedValue
    private int id;

    @NotNull
    String name;

    @ManyToOne
    @JoinColumn(name = "FAMILY_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Family family;

    @ManyToOne
    @JoinColumn(name = "TYPE_NAME") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Type type;

    @ManyToOne
    @JoinColumn(name = "SUPPLIER_NAME")
    Supplier supplier;

    //simulação
    @OneToMany(mappedBy = "material", cascade = CascadeType.REMOVE)
    private List<Variante> variantes;

    MaterialState status;

    @Version private int version;

    public Material() {
        this.variantes = new LinkedList<Variante>();
    }

    public Material(String name, Family family, Type type, Supplier supplier, MaterialState status) {
        this.name = name;
        this.family = family;
        this.type = type;
        this.supplier = supplier;
        this.status = status;
        variantes = new LinkedList<Variante>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSection() {
        return name;
    }

    public void setSection(String name) {
        this.name = name;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> variantes) {
        this.variantes = variantes;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void addVariante(Variante variante) {
        variantes.add(variante);
    }

    public void removeVariante(Variante variante) {
        variantes.remove(variante);
    }

    public MaterialState getStatus() {
        return status;
    }

    public void setStatus(MaterialState status) {
        this.status = status;
    }
}
