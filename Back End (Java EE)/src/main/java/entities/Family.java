package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllFamilies",
                query = "SELECT f FROM Family f ORDER BY f.name" // JPQL
        )
})
public class Family {
    @Id
    String name;
    String description;

    @OneToMany(mappedBy = "family", cascade = CascadeType.REMOVE)
    List<Material> materials;


    public Family() {
        this.materials = new LinkedList<>();
    }

    public Family(String name, String description) {
        this.name = name;
        this.description = description;
        materials = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}
