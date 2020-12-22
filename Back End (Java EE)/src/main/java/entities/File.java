package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getProjectFiles",
                query = "SELECT doc FROM File doc WHERE doc.project.name = :username")
})


public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @NotNull
    String fileName;
    @NotNull
    String observations;
    @NotNull
    String filePath;

    @ManyToOne
    @JoinColumn(name = "PROJECT_CODE")
    @NotNull
    Project project;

    public File(){}

    public File(String fileName, @NotNull String observations, @NotNull String filePath, Project project) {
        this.fileName = fileName;
        this.observations = observations;
        this.filePath = filePath;
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
