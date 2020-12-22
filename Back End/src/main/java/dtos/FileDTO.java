package dtos;

import entities.Project;

import java.io.Serializable;

public class FileDTO implements Serializable {
    int id;
    String namePath, observations,filePath, project;

    public FileDTO(){}

    public FileDTO(int id, String namePath, String observations, String filePath, String project) {
        this.id = id;
        this.namePath = namePath;
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

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String name) {
        this.namePath = name;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
