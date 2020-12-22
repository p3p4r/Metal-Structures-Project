package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdjudications",
                query = "SELECT a FROM Adjudication a ORDER BY a.id" // JPQL
        )
})
public class Adjudication {
    @Id @GeneratedValue
    int id;

    @NotNull
    TypeOfAdjudication adjudicate;

    String observation;

    @ManyToOne
    @JoinColumn(name = "CLIENT_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Client client;

    @ManyToOne
    @JoinColumn(name = "STRUCTURE_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Structure structure;

    /**************Testar***************/
    @ManyToOne
    @JoinColumn(name = "PROJECT_CODE")
    @NotNull
    Project project;
    /**********************************/

    @ManyToMany(mappedBy = "adjudications")
    List<Variante> variantes;

    public Adjudication() {
        variantes = new LinkedList<Variante>();
    }

    public Adjudication(@NotNull TypeOfAdjudication adjudicate, @NotNull String observation, @NotNull Client client, @NotNull Structure structure, @NotNull Project project) {
        this.adjudicate = adjudicate;
        this.observation = observation;
        this.client = client;
        this.project = project;
        this.structure = structure;
        variantes = new LinkedList<Variante>();
    }

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> variantes) {
        this.variantes = variantes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOfAdjudication getAdjudicate() {
        return adjudicate;
    }

    public void setAdjudicate(TypeOfAdjudication adjudicate) {
        this.adjudicate = adjudicate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public void addVariante(Variante variante) {
        variantes.add(variante);
    }

    public void removeVariante(Variante variante) {
        variantes.remove(variante);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
