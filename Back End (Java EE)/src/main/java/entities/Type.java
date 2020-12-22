package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllTypes",
                query = "SELECT t FROM Type t ORDER BY t.name" // JPQL
        )
})
public class Type {
    @Id
    String name;

    String description;

    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
    List<Structure> structure;

    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
    List<Material> materials;

    public Type() {
        this.structure = new LinkedList<Structure>();
        this.materials = new LinkedList<Material>();
    }


    public Type(String name, String description) {
        this.name = name;
        this.description = description;
        structure = new LinkedList<Structure>();
        materials = new LinkedList<Material>();
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

    public List<Structure> getStructure() {
        return structure;
    }

    public void setStructure(List<Structure> structure) {
        this.structure = structure;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public void addStructure(Structure s){ structure.add(s); }

    public void removeStructure(Structure s){
        structure.remove(s);
    }

    public void addMaterial(Material m){ materials.add(m); }

    public void removeMaterial(Material m){
        materials.remove(m);
    }
}
