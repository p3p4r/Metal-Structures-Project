package dtos;

import entities.Client;
import entities.Structure;
import entities.TypeOfAdjudication;
import entities.Variante;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AdjudicationDTO implements Serializable {
    int id;

    TypeOfAdjudication adjudicate;

    String observation, client, project;
    int structure;
    List<VarianteDTO> variantes;

    public AdjudicationDTO() {
        variantes = new LinkedList<>();
    }

    public AdjudicationDTO(int id, TypeOfAdjudication adjudicate, String observation, String client, int structure, String project) {
        this.id = id;
        this.adjudicate = adjudicate;
        this.observation = observation;
        this.client = client;
        this.structure = structure;
        this.project = project;
        variantes = new LinkedList<>();
    }

    public AdjudicationDTO(AdjudicationDTO adjudicationDTO, List<VarianteDTO> varianteDTOS) {
        this(
                adjudicationDTO.id,
                adjudicationDTO.adjudicate,
                adjudicationDTO.observation,
                adjudicationDTO.client,
                adjudicationDTO.structure,
                adjudicationDTO.project
        );
        this.variantes = varianteDTOS;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getStructure() {
        return structure;
    }

    public void setStructure(int structure) {
        this.structure = structure;
    }

    public List<VarianteDTO> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<VarianteDTO> variantes) {
        this.variantes = variantes;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
