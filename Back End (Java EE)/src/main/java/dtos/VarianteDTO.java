package dtos;

import entities.Adjudication;
import entities.Material;
import entities.Structure;

import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class VarianteDTO implements Serializable {
    private int codigo;
    private int material;
    private String nome;
    private static double G = 78.5;

    private double weff_p;
    private double weff_n;
    private double ar;
    private double sigmaC;
    private double pp;

    private LinkedHashMap<Double,Double> mcr_p;
    private LinkedHashMap<Double,Double> mcr_n;
    private List<StructureDTO> structures;
    private List<AdjudicationDTO> adjudications;
    private List<SupplierDTO> suppliers;

    public VarianteDTO() {
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        this.structures = new LinkedList<>();
        this.adjudications = new LinkedList<>();
        this.suppliers = new LinkedList<>();
    }

    public VarianteDTO(int codigo, int material, String nome, double weff_p, double weff_n, double ar, double sigmaC) {
        this.codigo = codigo;
        this.material = material;
        this.nome = nome;
        this.weff_p = weff_p;
        this.weff_n = weff_n;
        this.ar = ar;
        this.sigmaC = sigmaC;
        this.pp = G * ar * Math.pow(10, -6);
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        this.structures = new LinkedList<>();
        this.adjudications = new LinkedList<>();
        this.suppliers = new LinkedList<>();
    }

    public VarianteDTO(VarianteDTO varianteDTO, List<AdjudicationDTO> adjudicationDTOS) {
        this(
                varianteDTO.codigo,
                varianteDTO.material,
                varianteDTO.nome,
                varianteDTO.weff_p,
                varianteDTO.weff_n,
                varianteDTO.ar,
                varianteDTO.sigmaC
        );
        this.adjudications = adjudicationDTOS;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static double getG() {
        return G;
    }

    public static void setG(double g) {
        G = g;
    }

    public double getWeff_p() {
        return weff_p;
    }

    public void setWeff_p(double weff_p) {
        this.weff_p = weff_p;
    }

    public double getWeff_n() {
        return weff_n;
    }

    public void setWeff_n(double weff_n) {
        this.weff_n = weff_n;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public double getSigmaC() {
        return sigmaC;
    }

    public void setSigmaC(double sigmaC) {
        this.sigmaC = sigmaC;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public LinkedHashMap<Double, Double> getMcr_p() {
        return mcr_p;
    }

    public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
        this.mcr_p = mcr_p;
    }

    public LinkedHashMap<Double, Double> getMcr_n() {
        return mcr_n;
    }

    public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
        this.mcr_n = mcr_n;
    }

    public List<StructureDTO> getStructures() {
        return structures;
    }

    public void setStructures(List<StructureDTO> structures) {
        this.structures = structures;
    }

    public List<AdjudicationDTO> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<AdjudicationDTO> adjudications) {
        this.adjudications = adjudications;
    }

    public List<SupplierDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SupplierDTO> suppliers) {
        this.suppliers = suppliers;
    }
}
