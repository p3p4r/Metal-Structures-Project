package dtos;

import entities.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProjectDTO implements Serializable {
    String name;
    String clientUsername;
    String draftsmanUsername;

    ProjectStatus status;

    List<FileDTO> files;
    List<StructureDTO> structures;
    List<AdjudicationDTO> adjudication;

    public ProjectDTO(){
        this.files = new LinkedList<>();
        this.structures = new LinkedList<>();
        this.adjudication = new LinkedList<>();
    }

    public ProjectDTO(String name, String clientUsername, String draftsmanUsername,ProjectStatus status) {
        this.name = name;
        this.clientUsername = clientUsername;
        this.draftsmanUsername = draftsmanUsername;
        this.status = status;
        this.files = new LinkedList<>();
        this.structures = new LinkedList<>();
        this.adjudication = new LinkedList<>();
    }

    public ProjectDTO(ProjectDTO projectDTO, List<FileDTO> fileDTOS, List<StructureDTO> structuresDTO){
        this(
                projectDTO.name,
                projectDTO.clientUsername,
                projectDTO.draftsmanUsername,
                projectDTO.status
        );
        this.files = fileDTOS;
        this.structures = structuresDTO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getDraftsmanUsername() {
        return draftsmanUsername;
    }

    public void setDraftsmanUsername(String draftsmanUsername) {
        this.draftsmanUsername = draftsmanUsername;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public List<StructureDTO> getStructures() {
        return structures;
    }

    public void setStructures(List<StructureDTO> structures) {
        this.structures = structures;
    }

    public List<AdjudicationDTO> getAdjudication() {
        return adjudication;
    }

    public void setAdjudication(List<AdjudicationDTO> adjudication) {
        this.adjudication = adjudication;
    }
}
