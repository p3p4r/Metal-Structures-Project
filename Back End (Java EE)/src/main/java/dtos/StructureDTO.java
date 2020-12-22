package dtos;

import entities.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class StructureDTO implements Serializable {
    int id;
    String name, dimensions, calculationParameters, type, project,draftsman;
    List<VarianteDTO> variantes;
    // TODO - Dimensions ????
    Date create_at,update_at;
    List<AdjudicationDTO> adjudications;

    public StructureDTO(){
        this.variantes = new LinkedList<>();
        this.adjudications = new LinkedList<>();
    }

    public StructureDTO(int id, String name, String dimensions, String calculationParameters, String type, String project, String draftsman, Date create_at, Date update_at) {
        this.id = id;
        this.name = name;
        this.dimensions = dimensions;
        this.calculationParameters = calculationParameters;
        this.type = type;
        this.project = project;
        this.draftsman = draftsman;
        this.create_at = create_at;
        this.update_at = update_at;
        this.variantes = new LinkedList<>();
        this.adjudications = new LinkedList<>();
    }

    public StructureDTO(StructureDTO toDTONoLists, List<VarianteDTO> varianteDTOS, List<AdjudicationDTO> adjudicationDTOS){
        this(
            toDTONoLists.id,
            toDTONoLists.name,
            toDTONoLists.dimensions,
            toDTONoLists.calculationParameters,
            toDTONoLists.type,
            toDTONoLists.project,
            toDTONoLists.draftsman,
            toDTONoLists.create_at,
            toDTONoLists.update_at
        );
        this.variantes = varianteDTOS;
        this.adjudications = adjudicationDTOS;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCalculationParameters() {
        return calculationParameters;
    }

    public void setCalculationParameters(String calculationParameters) {
        this.calculationParameters = calculationParameters;
    }

    public List<VarianteDTO> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<VarianteDTO> variantes) {
        this.variantes = variantes;
    }

    public String getDraftsman() {
        return draftsman;
    }

    public void setDraftsman(String draftsman) {
        this.draftsman = draftsman;
    }

    public List<AdjudicationDTO> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<AdjudicationDTO> adjudications) {
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

}
