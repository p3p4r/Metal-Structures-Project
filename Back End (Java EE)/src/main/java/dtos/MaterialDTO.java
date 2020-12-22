package dtos;

import entities.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class MaterialDTO implements Serializable {
    int id;
    String name;
    String family,type, supplier;
    MaterialState status;
    private List<VarianteDTO> variantes;

    public MaterialDTO(){
        this.variantes = new LinkedList<>();
    }

    public MaterialDTO(int id,String name,String family, String type,String supplier, MaterialState status) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.type = type;
        this.supplier = supplier;
        this.status = status;
        this.variantes = new LinkedList<>();
    }

    public MaterialDTO(MaterialDTO materialDTO, List<VarianteDTO> varianteDTOS) {
        this(
                materialDTO.id,
                materialDTO.name,
                materialDTO.family,
                materialDTO.type,
                materialDTO.supplier,
                materialDTO.status
        );
        this.variantes = varianteDTOS;
    }

    public long getId() {
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public List<VarianteDTO> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<VarianteDTO> variantes) {
        this.variantes = variantes;
    }

    public MaterialState getStatus() {
        return status;
    }

    public void setStatus(MaterialState status) {
        this.status = status;
    }
}
