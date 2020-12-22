package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllStructures",
                query = "SELECT s FROM Structure s ORDER BY s.id" // JPQL
        )
})
public class Structure {
    @Id
    @GeneratedValue
    int id;

    @NotNull
    String name;

    @ManyToMany(mappedBy = "structures")
    List<Variante> variantes;

    // TODO - Dimensions ????
    String dimensions;
    // TODO - parametros de calculo ????
    String calculationParameters;

    @ManyToOne
    @JoinColumn(name = "TYPE_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Type type;

    @ManyToOne
    @JoinColumn(name = "PROJECT_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Project project;

    @ManyToOne
    @JoinColumn(name = "DRAFTSMAN_CODE") // name of the column added to 'STUDENTS' table foreign key
    @NotNull
    Draftsman draftsman;

    Date create_at;

    Date update_at;


    @OneToMany(mappedBy = "structure", cascade = CascadeType.REMOVE)
    List<Adjudication> adjudications;

    public Structure(){
        this.variantes = new LinkedList<Variante>();
        this.adjudications = new LinkedList<Adjudication>();
    }

    public Structure(@NotNull String name, @NotNull String dimensions, @NotNull String calculationParameters, @NotNull Type type, @NotNull Project project, @NotNull Draftsman draftsman,  Date create_at,  Date update_at) {
        //this.id = id;
        this.name = name;
        this.dimensions = dimensions;
        this.calculationParameters = calculationParameters;
        this.type = type;
        this.project = project;
        this.draftsman = draftsman;
        this.create_at = create_at;
        this.update_at = update_at;
        this.variantes = new LinkedList<Variante>();
        this.adjudications = new LinkedList<Adjudication>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getcalculationParameters() {
        return calculationParameters;
    }

    public void setcalculationParameters(String calculationParameters) {
        this.calculationParameters = calculationParameters;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getCalculationParameters() {
        return calculationParameters;
    }

    public void setCalculationParameters(String calculationParameters) {
        this.calculationParameters = calculationParameters;
    }

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> variantes) {
        this.variantes = variantes;
    }

    public Draftsman getDraftsman() {
        return draftsman;
    }

    public void setDraftsman(Draftsman draftsman) {
        this.draftsman = draftsman;
    }

    public List<Adjudication> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<Adjudication> adjudications) {
        this.adjudications = adjudications;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public void addAdjudication(Adjudication a) {
        adjudications.add(a);
    }

    public void addVariante(Variante v) {
        variantes.add(v);
    }
}
