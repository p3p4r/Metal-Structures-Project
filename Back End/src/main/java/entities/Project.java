package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
    name = "PROJECTS",
    uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"})
)
@NamedQueries({
    @NamedQuery(
        name = "getAllProjects",
        query = "SELECT p FROM Project p ORDER BY p.name" // JPQL
    )
})
public class Project {

    @Id
    String name;

    @ManyToOne
    @JoinColumn(name = "CLIENT_USERNAME") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Client client;

    @ManyToOne
    @JoinColumn(name = "DRAFTSMAN_USERNAME") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Draftsman draftsman;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    List<File> files;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    List<Structure> structures;

    @NotNull
    ProjectStatus status;

    /***************TESTAR***************/
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    List<Adjudication> adjudications;

    @Version private int version;

    public Project(){
        this.files = new LinkedList<>();
        this.structures = new LinkedList<>();
        this.adjudications = new LinkedList<Adjudication>();
    }

    public Project(String name, Client client, Draftsman draftsman,ProjectStatus status) {
        this.name = name;
        this.client = client;
        this.draftsman = draftsman;
        this.status = status;
        files = new LinkedList<>();
        structures = new LinkedList<>();
        this.adjudications = new LinkedList<Adjudication>();
    }

    public ProjectStatus getProjectStatus() {
        return status;
    }

    public void setProjectStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Draftsman getDraftsman() {
        return draftsman;
    }

    public void setDraftsman(Draftsman draftsman) {
        this.draftsman = draftsman;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
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

    public void addFile(File f){ files.add(f); }

    public void removeFile(File f){
        files.remove(f);
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<Adjudication> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<Adjudication> adjudications) {
        this.adjudications = adjudications;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addAdjudication(Adjudication a) {
        adjudications.add(a);
    }
}
