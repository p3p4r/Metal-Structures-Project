package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllDraftsmen",
                query = "SELECT d FROM Draftsman d ORDER BY d.username" // JPQL
        )
})
public class Draftsman extends User {

    @OneToMany(mappedBy = "draftsman", cascade = CascadeType.REMOVE)
    List<Project> projects;

    @OneToMany(mappedBy = "draftsman", cascade = CascadeType.REMOVE)
    List<Structure> structures;

    public Draftsman(){
        super();
        this.projects = new LinkedList<>();
        this.structures = new LinkedList<>();
    }

    public Draftsman(String username, @NotNull String password, @NotNull String name, @NotNull String address, @NotNull String contactPerson, @Email String email) {
        super(username, password, name, address, contactPerson, email);
        this.projects = new LinkedList<Project>();
        this.structures = new LinkedList<>();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project p){ projects.add(p); }

    public void removeProject(Project p){
        projects.remove(p);
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public void addStructure(Structure s){ structures.add(s); }

    public void removeStructure(Structure s){
        structures.remove(s);
    }
}
